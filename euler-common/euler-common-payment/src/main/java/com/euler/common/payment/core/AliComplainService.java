package com.euler.common.payment.core;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.FileItem;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.*;
import com.euler.common.payment.bean.AliComplaintQueryDto;
import com.euler.common.payment.bean.ComplainCallable;
import com.euler.common.payment.config.AliPayConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class AliComplainService implements IAliComplaintService {

    private String defaultCharset = "UTF-8";


    @Autowired
    private AliPayConfig aliPayConfig;

    private final static String signType = "RSA2";

    private AlipayClient alipayClient = null;


    @PostConstruct
    public void init() {
        alipayClient = new DefaultAlipayClient(aliPayConfig.getGateway(), aliPayConfig.getAppid(), aliPayConfig.getPrivateKey(), "json", defaultCharset, aliPayConfig.getPublicKey(), signType);

    }


    /**
     * 投诉详情
     *
     * @param id 投诉id
     * @return
     */
    @SneakyThrows
    @Override
    public String getComplainInfo(String id) {
        try {

            AlipayMerchantTradecomplainQueryRequest request = new AlipayMerchantTradecomplainQueryRequest();
            Map map = new HashMap<String, String>();
            map.put("complain_event_id", id);

            request.setBizContent(JsonHelper.toJson(map));
            AlipayMerchantTradecomplainQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return JsonHelper.toJson(response);

            } else {
                return null;
            }

        } catch (Exception e) {
            log.error("支付宝投诉--投诉详情，获取异常，{}", id, e);
        }
        return null;
    }

    /**
     * 查询投诉分页列表
     *
     * @param complaintQueryDto
     * @return
     */
    @Override
    public String getComplainList(AliComplaintQueryDto complaintQueryDto) {
        try {
            AlipayMerchantTradecomplainBatchqueryRequest request = new AlipayMerchantTradecomplainBatchqueryRequest();
            request.setBizContent(JsonHelper.toJson(complaintQueryDto));
            AlipayMerchantTradecomplainBatchqueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return JsonHelper.toJson(response.getTradeComplainInfos());
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("支付宝投诉--投诉列表，获取异常，{}", JsonHelper.toJson(complaintQueryDto), e);

        }
        return null;
    }


    /**
     * 投诉回调
     *
     * @param request
     * @param response
     * @param callable
     */
    @SneakyThrows
    @Override
    public void AsyncNotify(HttpServletRequest request, HttpServletResponse response, ComplainCallable<Boolean> callable) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = ServletUtils.getParamMap(ServletUtils.getRequest());
        if (params == null || params.isEmpty()) {
            String params2 = ServletUtils.getParas();
            log.info("支付宝投诉--异步回调原始参数2：{}", params2);
            params = HttpClientUtil.StringToMap(params2);
        }
        log.info("支付宝投诉--异步回调--参数：{}", JsonUtils.toJsonString(params));

        boolean flag = AlipaySignature.rsaCheckV1(params, aliPayConfig.getPublicKey(), defaultCharset, signType);
        response.setHeader("content-type", "text/html;charset=UTF-8");

        if (flag) {

            String complainId = params.get("complain_event_id");

            if (StringUtils.isNotBlank(complainId)) {
                String info = getComplainInfo(complainId);
                try {
                    callable.call(info);
                } catch (Exception e) {
                    log.error("支付宝投诉--异步回调--回调入库异常，{}", info, e);
                    response.getWriter().write("fail");
                }

            } else {
                log.error("支付宝投诉--异步回调--获取投诉id失败，{}", complainId);
            }
            log.info("支付宝投诉--异步回调--校验参数成功，{}", complainId);

            response.getWriter().write("success");
        } else {
            log.info("支付宝投诉--异步回调--校验参数失败");
            response.getWriter().write("fail");

        }
    }


    /**
     * 商家处理交易投诉
     *
     * @param id               投诉id
     * @param feedback_code    反馈类目ID
     *                         00:使用体验保障金退款；
     *                         02:通过其他方式退款;
     *                         03:已发货;
     *                         04:其他;
     *                         05:已完成售后服务;
     *                         06:非我方责任范围；
     * @param feedback_content 反馈内容，字数不超过200个字
     * @param feedback_images  商家处理投诉时反馈凭证的图片id，多个逗号隔开（图片id可以通过"商户上传处理图片"接口获取）
     * @param operator         处理投诉人，字数不超过6个字
     */
    @SneakyThrows
    @Override
    public R feedback(String id, String feedback_code, String feedback_content, String feedback_images, String operator) {
        try {
            AlipayMerchantTradecomplainFeedbackSubmitRequest request = new AlipayMerchantTradecomplainFeedbackSubmitRequest();
            request.setComplainEventId(id);
            request.setFeedbackCode(feedback_code);
            request.setFeedbackContent(feedback_content);
            if (StringUtils.isNotBlank(feedback_images))
                request.setFeedbackImages(feedback_images);
            if (StringUtils.isNotBlank(operator))
                request.setOperator(operator);
            AlipayMerchantTradecomplainFeedbackSubmitResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return R.ok();
            } else {
                return R.fail(response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("支付宝投诉--商家处理交易投诉，异常,id:{},feedback_code:{},feed_content:{}", id, feedback_code, feedback_content, e);
            return R.fail("商家处理交易投诉异常");

        }

    }

    /**
     * 商家留言回复
     *
     * @param id            投诉id
     * @param reply_content 回复留言内容
     * @param reply_images  商家处理问题反馈时的回复凭证的图片id，多个逗号隔开（图片id可以通过"商户上传处理图片"接口获取）
     * @return
     */
    @SneakyThrows
    @Override
    public R reply(String id, String reply_content, String reply_images) {
        try {
            AlipayMerchantTradecomplainReplySubmitRequest request = new AlipayMerchantTradecomplainReplySubmitRequest();
            Map map = new HashMap<String, String>();
            map.put("complain_event_id", id);
            map.put("reply_content", reply_content);
            if (StringUtils.isNotBlank(reply_images))
                map.put("reply_images", reply_images);


            request.setBizContent(JsonHelper.toJson(map));
            AlipayMerchantTradecomplainReplySubmitResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return R.ok();
            } else {
                return R.fail(response.getSubMsg());
            }
        } catch (Exception e) {
            log.error("支付宝投诉--商家留言回复，异常,id:{},reply_content:{}", id, reply_content, e);
            return R.fail("商家留言回复异常");
        }
    }


    /**
     * 商户上传处理图片
     *
     * @return
     */
    @SneakyThrows
    @Override
    public R<String> uploadImg(FileItem image_content, String image_type) {
        AlipayMerchantImageUploadRequest request = new AlipayMerchantImageUploadRequest();
        request.setImageType(image_type);

        request.setImageContent(image_content);
        AlipayMerchantImageUploadResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return R.ok("", response.getImageId());
        } else {

            return R.fail(response.getSubMsg());
        }
    }

}
