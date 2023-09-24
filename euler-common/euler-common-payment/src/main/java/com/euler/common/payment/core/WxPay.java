package com.euler.common.payment.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.payment.bean.*;
import com.euler.common.payment.config.WxPayProperties;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.utils.WxPayHtmlUtils;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.util.*;

@Component
@Slf4j
public class WxPay implements Pay<Trade> {

    @Autowired
    private WxPayService wxService;
    @Autowired
    private WxPayProperties wxPayProperties;

    /**
     * 支付
     *
     * @param trade 订单
     * @return 订单凭证
     */
    @SneakyThrows
    @Override
    public TradeToken<String> pay(Trade trade) {

        WxPayUnifiedOrderV3Request unifiedOrderV3Request = new WxPayUnifiedOrderV3Request();

        BigDecimal tradeAmount = new BigDecimal(trade.getTotalAmount());

        unifiedOrderV3Request.setAmount(new WxPayUnifiedOrderV3Request.Amount().setCurrency("CNY").setTotal(NumberUtil.mul(tradeAmount, 100).intValue()))
            .setOutTradeNo(trade.getOutTradeNo())
            .setDescription(trade.getBody())
            .setTimeExpire(DateUtil.offsetMinute(new Date(), 30).toString(DatePattern.UTC_WITH_XXX_OFFSET_FORMAT));
        TradeTypeEnum tradeTypeEnum = TradeTypeEnum.APP;
        var sceneInfo = new WxPayUnifiedOrderV3Request.SceneInfo().setPayerClientIp(ServletUtils.getClientIP());


        if (trade.getPayType().equals("h5")) {
            tradeTypeEnum = TradeTypeEnum.H5;
            sceneInfo.setH5Info(new WxPayUnifiedOrderV3Request.H5Info().setType("Wap"));
            unifiedOrderV3Request.setSceneInfo(sceneInfo);
            unifiedOrderV3Request.setAppid(wxPayProperties.getAppIdH5());
        }


        try {

            val result = wxService.createOrderV3(tradeTypeEnum, unifiedOrderV3Request);
            log.info("微信支付--统一下单--微信返回：result：{}，trade:{}", JsonUtils.toJsonString(result), JsonUtils.toJsonString(trade));
            if (result != null) {
                if (tradeTypeEnum.equals(TradeTypeEnum.APP)) {
                    return new TradeToken<String>() {
                        @Override
                        public String value() {
                            return JsonUtils.toJsonString(result);
                        }

                        @Override
                        public String businessOrderId() {
                            return trade.getBusinessOrderId();
                        }

                        @Override
                        public Boolean success() {
                            return true;
                        }
                    };
                } else {
                    return new TradeToken<String>() {
                        @Override
                        public String value() {
                            return WxPayHtmlUtils.readUrl(Convert.toStr(result));
                        }

                        @Override
                        public String businessOrderId() {
                            return trade.getBusinessOrderId();
                        }

                        @Override
                        public Boolean success() {
                            return true;
                        }
                    };
//                    String htmls = HttpRequest.get(result.getH5Url()).header("Referer", "https://app.eulertu.cn").execute().body();
//                    String regExp = "weixin://wap/pay\\S*\\b";
//                    Pattern pattern = Pattern.compile(regExp);//匹配的模式
//                    Matcher matcher = pattern.matcher(htmls);
//                    if (matcher.find()) {
//                        // 将匹配当前正则表达式的字符串即文件名称进行赋值
//                        resId = matcher.group();
//                    }
                }
            }

        } catch (WxPayException e) {
            log.error("微信支付--统一下单--异常：trade:{}", JsonUtils.toJsonString(trade), e);
            throw e;
        }

        TradeToken<String> tradeToken = new TradeToken<String>() {
            @Override
            public String value() {
                return "下单失败";
            }

            @Override
            public Boolean success() {
                return false;
            }
        };
        return tradeToken;

    }


    /**
     * 订单状态查询
     *
     * @param trade 订单
     * @return 订单状态
     */
    @Override
    public TradeResult query(Trade trade) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WxQuery);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        try {
            val result = wxService.queryOrderV3(trade.getTradeNo(), trade.getOutTradeNo());
            log.info("微信支付--订单状态查询--微信返回：result：{}，trade:{}", JsonUtils.toJsonString(result), JsonUtils.toJsonString(trade));

            if (result != null) {
                tradeResult.setMsg("");
                tradeResult.setTradeNo(result.getTransactionId());
                tradeResult.setSuccess(true);
                tradeResult.setTradeStatus(result.getTradeState());
                tradeResult.setBuyerLogonId(result.getPayer() != null ? result.getPayer().getOpenid() : "");

            }
        } catch (WxPayException e) {
            tradeResult.setMsg(e.getReturnMsg());
            tradeResult.setCode(e.getReturnCode());
            tradeResult.setSubCode(e.getErrCode());
            tradeResult.setSubMsg(e.getErrCodeDes());
            tradeResult.setSuccess(false);
            log.info("微信支付--订单状态查询--异常：trade:{}", JsonUtils.toJsonString(trade), e);
        }


        return tradeResult;
    }

    /**
     * 退款申请
     *
     * @param trade 订单
     */
    @SneakyThrows
    @Override
    public TradeResult refund(Trade trade) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WxRefund);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        BigDecimal tradeAmount = new BigDecimal(trade.getRefundAmount());
        int amount = NumberUtil.mul(tradeAmount, 100).intValue();
        request.setTransactionId(trade.getTradeNo())
            .setAmount(new WxPayRefundV3Request.Amount().setRefund(amount).setCurrency("CNY").setTotal(amount))
            .setReason(trade.getRefundReason())
            .setOutTradeNo(trade.getOutTradeNo())
            .setOutRefundNo(trade.getOutRequestNo())
            .setNotifyUrl(wxPayProperties.getRefundNotifyUrl());
        try {
            val result = wxService.refundV3(request);
            log.info("微信支付--退款申请--微信返回：result：{}，trade:{}", JsonUtils.toJsonString(result), JsonUtils.toJsonString(trade));

            if (result != null) {
                tradeResult.setMsg("");
                tradeResult.setTradeNo(result.getTransactionId());
                tradeResult.setSuccess(true);
                tradeResult.setTradeStatus(result.getStatus());
            }

        } catch (WxPayException e) {
            tradeResult.setMsg(e.getReturnMsg());
            tradeResult.setCode(e.getReturnCode());
            tradeResult.setSubCode(e.getErrCode());
            tradeResult.setSubMsg(e.getErrCodeDes());
            tradeResult.setSuccess(false);
            log.info("微信支付--退款申请--异常：trade:{}", JsonUtils.toJsonString(trade), e);

        }
        return tradeResult;
    }

    /**
     * 退款查询
     *
     * @param trade 订单
     */
    @Override
    public TradeResult refundQuery(Trade trade) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WxRefundQuery);
        tradeResult.setOutTradeNo(trade.getOutTradeNo());

        try {
            val result = wxService.refundQueryV3(trade.getOutRequestNo());
            log.info("微信支付--退款查询--微信返回：result：{}，trade:{}", JsonUtils.toJsonString(result), JsonUtils.toJsonString(trade));

            if (result != null) {
                tradeResult.setMsg("");
                tradeResult.setTradeNo(result.getTransactionId());
                tradeResult.setSuccess(true);
                tradeResult.setTradeStatus(result.getStatus());

            }
        } catch (WxPayException e) {

            tradeResult.setMsg(e.getReturnMsg());
            tradeResult.setCode(e.getReturnCode());
            tradeResult.setSubCode(e.getErrCode());
            tradeResult.setSubMsg(e.getErrCodeDes());
            tradeResult.setSuccess(false);
            log.info("微信支付--退款查询--异常：trade:{}", JsonUtils.toJsonString(trade), e);

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
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WxQuery);
        String requestData = WxPayHtmlUtils.fetchRequest2Str(request);
        SignatureHeader header = WxPayHtmlUtils.fetchRequest2SignatureHeader(request);
        response.setHeader("content-type", "application/json;charset=UTF-8");
        try {
            var v3Result = this.wxService.parseOrderNotifyV3Result(requestData, header);

            if (v3Result != null && v3Result.getResult() != null) {
                var result = v3Result.getResult();
                log.info("微信支付--异步回调--微信返回：requestData:{},header:{},result:{}", requestData, JsonUtils.toJsonString(header), JsonUtils.toJsonString(result));

                tradeResult.setTradeNo(result.getTransactionId());
                tradeResult.setTradeStatus(result.getTradeState());//状态

                tradeResult.setBuyerLogonId(result.getPayer() != null ? result.getPayer().getOpenid() : "");
                tradeResult.setSuccess(true);
                tradeResult.setOutTradeNo(result.getOutTradeNo());
                tradeResult.setOutBizNo("");
                tradeResult.setMsg(result.getTradeStateDesc());
                tradeResult.setCode("");
                callable.call(tradeResult);
                response.getWriter().write(JsonUtils.toJsonString(WxPayResult.ok()));
            } else {
                log.error("微信支付--异步回调--错误：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header));
                response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));
            }

        } catch (WxPayException e) {
            log.error("微信支付--异步回调--异常：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header), e);
            response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));

        }
    }

    /**
     * 微信退款异步回调
     *
     * @param request  请求
     * @param response 输出
     * @param callable 业务回调
     */
    @SneakyThrows
    @Override
    public void AsyncRefundNotify(HttpServletRequest request, HttpServletResponse response, CallablePlus callable) {
        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WxRefundQuery);
        String requestData = WxPayHtmlUtils.fetchRequest2Str(request);
        SignatureHeader header = WxPayHtmlUtils.fetchRequest2SignatureHeader(request);
        response.setHeader("content-type", "application/json;charset=UTF-8");

        try {
            var v3Result = this.wxService.parseRefundNotifyV3Result(requestData, header);

            if (v3Result != null && v3Result.getResult() != null) {
                var result = v3Result.getResult();
                log.info("微信退款--异步回调--微信返回：requestData:{},header:{},result:{}", requestData, JsonUtils.toJsonString(header), JsonUtils.toJsonString(result));

                tradeResult.setTradeNo(result.getTransactionId());//微信支付单号
                tradeResult.setTradeStatus(result.getRefundStatus());//状态
                tradeResult.setSuccess(true);
                tradeResult.setOutTradeNo(result.getOutTradeNo());//业务支付单号
                tradeResult.setOutBizNo(result.getOutRefundNo());//业务退款单号
                tradeResult.setRefundTradeNo(result.getRefundId());//微信退款单号
                tradeResult.setMsg("通知成功");
                tradeResult.setCode("");
                callable.call(tradeResult);
                response.getWriter().write(JsonUtils.toJsonString(WxPayResult.ok()));
            } else {
                log.error("微信退款--异步回调--错误：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header));
                response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));
            }

        } catch (WxPayException e) {
            log.error("微信退款--异步回调--异常：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header), e);
            response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));

        }

    }

    /**
     * 提现到微信
     *
     * @param amount 提现金额，单位是分
     * @param remark 备注
     * @param openId openId
     * @return
     */
    public String cashOutToWX(BigDecimal amount, String remark, String openId) {

        String requestUrl = "https://api.mch.weixin.qq.com/v3/transfer/batches";
        Map<String, Object> postMap = new HashMap<String, Object>();

        postMap.put("appid", wxPayProperties.getAppId());
        // 商家批次单号
        String batchNo = IdUtil.getSnowflake(0, 0).nextIdStr();
        postMap.put("out_batch_no", batchNo);
        // 该笔转账的名称
        postMap.put("batch_name", DateUtil.format(DateUtil.parse(DateUtil.now()), "yyyyMMdd") + "微信提现");
        // 转账说明，UTF8编码，最多允许32个字符
        postMap.put("batch_remark", StringUtils.isEmpty(remark)? "备注": remark);
        // 总金额, 转账金额单位为分
        postMap.put("total_amount", BaseWxPayRequest.yuanToFen(amount.toString()));
        // 转账总笔数
        postMap.put("total_num", 1);

        List<Map> list = new ArrayList<>();
        Map<String, Object> subMap = new HashMap<>(4);
        // 商家明细单号
        subMap.put("out_detail_no", batchNo);
        // 转账金额
        subMap.put("transfer_amount", BaseWxPayRequest.yuanToFen(amount.toString()));
        // 转账备注
        subMap.put("transfer_remark", "明细备注");
        // 用户在直连商户应用下的用户标示
        subMap.put("openid", openId);
        list.add(subMap);
        postMap.put("transfer_detail_list", list);

        String basePath= this.getClass().getResource("/").getPath().replaceFirst("/", "");

        // classpath:cert/apiclient_cert.pem
        String privateCertFile = wxPayProperties.getPrivateCertPath().replace("classpath:", "");
        // 商户私钥证书路径
        String privateCertPath = basePath + privateCertFile;
        // 获取证书
        X509Certificate certificate = WxPayHtmlUtils.getCertificate(FileUtil.getInputStream(privateCertPath));
        // 获取商户的证书序列号, 这里是wx的jar包生成的序列号
        String serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
        log.info("=======商户的证书序列号:========" + serialNo);
        // classpath:cert/apiclient_key.pem
        String privateKeyFile = wxPayProperties.getPrivateKeyPath().replace("classpath:", "");
        // 商户私钥证书路径
        String privateKeyPath = basePath + privateKeyFile;

        // 发起转账操作
        String resStr = WxPayHtmlUtils.postTransRequest(requestUrl, JsonUtils.toJsonString(postMap),
            wxPayProperties.getMchId(), serialNo, privateKeyPath);
        return resStr;
    }

}
