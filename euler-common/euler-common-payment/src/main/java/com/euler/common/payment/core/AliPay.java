package com.euler.common.payment.core;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.euler.common.core.utils.*;
import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.TradeToken;
import com.euler.common.payment.config.AliPayConfig;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.utils.WxPayHtmlUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Component
public class AliPay implements Pay<Trade> {
    @Autowired
    private AliPayConfig aliPayConfig;

    private final static String signType = "RSA2";

    private AlipayClient alipayClient = null;

    @PostConstruct
    public void init() {
        alipayClient = new DefaultAlipayClient(aliPayConfig.getGateway(), aliPayConfig.getAppid(), aliPayConfig.getPrivateKey(), "json", defaultCharset, aliPayConfig.getPublicKey(), signType);
    }

    /**
     * 支付
     *
     * @param trade 订单
     * @return 订单凭证
     */
    @SneakyThrows
    @Override
    public TradeToken<String> pay(Trade trade) {
        if (trade.getPayType().equals("app"))
            return appPay(trade);
        else if (trade.getPayType().equals("h5"))
            return h5Pay(trade);

        return new TradeToken<String>() {
            @Override
            public String value() {
                return "不支持的支付方式";
            }

            @Override
            public Boolean success() {
                return false;
            }
        };
    }

    @SneakyThrows
    private TradeToken<String> appPay(Trade trade) {
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(trade.getBody());
        model.setSubject(trade.getSubject());
        model.setOutTradeNo(trade.getOutTradeNo());
        model.setTimeoutExpress("30m");

        model.setTotalAmount(trade.getTotalAmount());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());

        //这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
        return new TradeToken<String>() {
            @Override
            public String value() {
                return response.getBody();
            }

            @Override
            public Boolean success() {
                return response.isSuccess();
            }

            @Override
            public String businessOrderId() {
                return response.isSuccess() ? trade.getBusinessOrderId() : "";
            }
        };
    }

    @SneakyThrows
    private TradeToken<String> h5Pay(Trade trade) {
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", trade.getOutTradeNo());
        bizContent.put("total_amount", trade.getTotalAmount());
        bizContent.put("subject", trade.getSubject());
        bizContent.put("product_code", "QUICK_WAP_WAY");
        bizContent.put("timeout_express", "30m");
        request.setBizContent(bizContent.toString());
        AlipayTradeWapPayResponse response = alipayClient.pageExecute(request);

        String res = WxPayHtmlUtils.getAliH5Url(response.getBody());

        return new TradeToken<String>() {
            @Override
            public String value() {
                return res;
            }

            @Override
            public Boolean success() {
                return response.isSuccess();
            }

            @Override
            public String businessOrderId() {
                return response.isSuccess() ? trade.getBusinessOrderId() : "";
            }
        };
    }

    private String signRSA2(Map<String, String> params) {
        try {
            List<String> pairs = new ArrayList<String>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isBlank(entry.getValue())) {
                    continue;
                }
                pairs.add(entry.getKey() + "=" + entry.getValue());
            }
            Collections.sort(pairs, String.CASE_INSENSITIVE_ORDER);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < pairs.size(); i++) {
                builder.append(pairs.get(i));
                if (i + 1 < pairs.size()) {
                    builder.append("&");
                }
            }

            String unCodeStr = builder.toString();
            log.info("支付宝支付签名字符串：{}", unCodeStr);
            RSA rsa = new RSA(aliPayConfig.getPrivateKey(), null);
            String jsonRes = rsa.encryptBase64(unCodeStr, KeyType.PrivateKey);
            return jsonRes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将map集合中的参数按照url params参数格式拼接到url中
     *
     * @param params 参数集合
     * @param url    请求路径
     * @return
     */
    private String appendParamsToURL(Map<String, String> params, String url) {
        try {
            StringBuilder sb = new StringBuilder(64);

            if (StringUtils.isNotBlank(url)) {
                sb.append(url);
                sb.append("?");
            }

            for (String key : params.keySet()) {
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(params.get(key), defaultCharset));
                sb.append("&");
            }

            return sb.toString().replaceAll("(&|\\?)$", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 订单状态查询
     *
     * @param trade 订单
     * @return 订单状态
     */
    @SneakyThrows
    @Override
    public TradeResult query(Trade trade) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", trade.getOutTradeNo());
//bizContent.put("trade_no", "2014112611001004680073956707");
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        log.info("支付宝订单查询--订单id：{}支付宝返回数据：{}", trade.getOutTradeNo(), JsonUtils.toJsonString(response));
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.AliQuery);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        if (response != null) {
            tradeResult.setMsg(response.getMsg());
            tradeResult.setCode(response.getCode());
            tradeResult.setSubCode(response.getSubCode());
            tradeResult.setBuyerLogonId(response.getBuyerLogonId());
            tradeResult.setSubMsg(response.getSubMsg());
            tradeResult.setTradeNo(response.getTradeNo());
            tradeResult.setSuccess(response.isSuccess());
            tradeResult.setTradeStatus(StringUtils.isEmpty(response.getTradeStatus()) ? response.getSubCode() : response.getTradeStatus());
        }
        return tradeResult;
    }

    /**
     * 退款
     *
     * @param trade 订单
     */
    @SneakyThrows
    @Override
    public TradeResult refund(Trade trade) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", trade.getTradeNo());
        bizContent.put("out_trade_no", trade.getOutTradeNo());
        bizContent.put("refund_amount", trade.getRefundAmount());
        bizContent.put("out_request_no", trade.getOutRequestNo());
        bizContent.put("refund_reason", trade.getRefundReason());

        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.AliRefund);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        if (response != null) {
            tradeResult.setMsg(response.getMsg());
            tradeResult.setCode(response.getCode());
            tradeResult.setSubCode(response.getSubCode());
            tradeResult.setBuyerLogonId(response.getBuyerLogonId());
            tradeResult.setSubMsg(response.getSubMsg());
            tradeResult.setTradeNo(response.getTradeNo());
            tradeResult.setSuccess(response.isSuccess());
        }
        return tradeResult;
    }

    /**
     * 退款查询
     *
     * @param trade 订单
     */
    @SneakyThrows
    @Override
    public TradeResult refundQuery(Trade trade) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", trade.getTradeNo());
        bizContent.put("out_request_no", trade.getOutRequestNo());
        bizContent.put("out_trade_no", trade.getOutTradeNo());

        request.setBizContent(bizContent.toString());
        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.AliRefundQuery);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        if (response != null) {
            tradeResult.setMsg(response.getMsg());
            tradeResult.setCode(response.getCode());
            tradeResult.setSubCode(response.getSubCode());
            tradeResult.setSubMsg(response.getSubMsg());
            tradeResult.setTradeNo(response.getTradeNo());
            tradeResult.setSuccess(response.isSuccess());
            tradeResult.setTradeStatus(StringUtils.isEmpty(response.getRefundStatus()) ? response.getSubCode() : response.getRefundStatus());
        }
        return tradeResult;
    }

    /**
     * 异步回调
     *
     * @param request  请求
     * @param response 输出
     * @param callable 业务回调
     */
    @SneakyThrows
    @Override
    public void AsyncNotify(HttpServletRequest request, HttpServletResponse response, CallablePlus callable) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = ServletUtils.getParamMap(ServletUtils.getRequest());
        if (params==null||params.isEmpty()){
            String params2=ServletUtils.getParas();
            log.info("支付宝异步回调原始参数2：{}", params2);
            params= HttpClientUtil.StringToMap(params2);
        }
        log.info("支付宝异步回调--参数：{}", JsonUtils.toJsonString(params));

        boolean flag = AlipaySignature.rsaCheckV1(params, aliPayConfig.getPublicKey(), defaultCharset, signType);
        response.setHeader("content-type", "text/html;charset=UTF-8");

        if (flag) {
            TradeResult tradeResult = new TradeResult(PayTypeEnumd.AliMobilePay);
            tradeResult.setTradeNo(params.get("trade_no"));
            tradeResult.setTradeStatus(params.get("trade_status"));
            tradeResult.setBuyerLogonId(params.get("buyer_logon_id"));
            tradeResult.setSuccess(true);
            tradeResult.setOutTradeNo(params.get("out_trade_no"));
            tradeResult.setOutBizNo(params.get("out_biz_no"));
            tradeResult.setMsg("");
            tradeResult.setCode("10000");
            callable.call(tradeResult);
            log.info("支付宝异步回调--校验参数成功，{}", tradeResult.getOutTradeNo());

            response.getWriter().write("success");
        } else {
            log.info("支付宝异步回调--校验参数失败");
            response.getWriter().write("failure");
        }
    }

    /**
     * 退款异步回调
     *
     * @param request  请求
     * @param response 输出
     * @param callable 业务回调
     */
    @Override
    public void AsyncRefundNotify(HttpServletRequest request, HttpServletResponse response, CallablePlus callable) {
        AsyncNotify(request, response, callable);
    }

}
