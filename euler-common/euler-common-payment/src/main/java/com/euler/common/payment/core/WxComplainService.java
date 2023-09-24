package com.euler.common.payment.core;

import com.alibaba.fastjson.JSONObject;
import com.euler.common.core.utils.JsonHelper;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.ComplainCallable;
import com.euler.common.payment.bean.WxPayResult;
import com.euler.common.payment.config.WxPayProperties;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.utils.WxPayHtmlUtils;
import com.github.binarywang.wxpay.bean.complaint.*;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Component
@Slf4j
public class WxComplainService implements IWxComplaintService {

    @Autowired
    private WxPayService wxService;

    @Autowired
    private WxPayProperties wxPayProperties;

    /**
     * 创建投诉回调地址
     *
     * @param url 回调地址
     * @return
     */
    @SneakyThrows
    @Override
    public Boolean createNotifyUrl(String url) {
        var complaintNotifyUrlResult = wxService.getComplaintsService().addComplaintNotifyUrl(new ComplaintNotifyUrlRequest(url));
        if (complaintNotifyUrlResult != null && StringUtils.isNotBlank(complaintNotifyUrlResult.getUrl()))
            return true;
        return false;


    }

    /**
     * 更新投诉回调地址
     *
     * @param url 回调地址
     * @return
     */
    @SneakyThrows
    @Override
    public Boolean updateNotifyUrl(String url) {
        try {
            var complaintNotifyUrlResult = wxService.getComplaintsService().updateComplaintNotifyUrl(new ComplaintNotifyUrlRequest(url));
            if (complaintNotifyUrlResult != null && StringUtils.isNotBlank(complaintNotifyUrlResult.getUrl()))
                return true;
        } catch (Exception e) {
            log.error("微信投诉--更新投诉回调地址，异常,url:{}", url, e);

        }
        return false;
    }

    /**
     * 删除投诉回调地址
     *
     * @param url 回调地址
     * @return
     */
    @SneakyThrows
    @Override
    public Boolean deleteNotifyUrl(String url) {
        wxService.getComplaintsService().deleteComplaintNotifyUrl();
        return true;
    }

    /**
     * 查询投诉回调地址
     *
     * @return
     */
    @SneakyThrows
    @Override
    public String queryNotifyUrl() {
        try {
            var complaintResult = wxService.getComplaintsService().getComplaintNotifyUrl();
            if (complaintResult != null && StringUtils.isNotBlank(complaintResult.getUrl()))
                return complaintResult.getUrl();
        } catch (Exception e) {
            log.error("微信投诉--查询投诉回调地址，获取异常", e);
        }
        return null;
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
            var complaint = wxService.getComplaintsService().getComplaint(new ComplaintDetailRequest(id));
            return JsonHelper.toJson(complaint);
        } catch (Exception e) {
            log.error("微信投诉--投诉详情，获取异常，{}", id, e);
        }
        return null;
    }

    /**
     * 协商历史
     *
     * @param id 投诉id
     * @return
     */
    @SneakyThrows
    @Override
    public String getNegotiationHistoryList(String id) {

        try {
            NegotiationHistoryRequest request = new NegotiationHistoryRequest();
            request.setComplaintId(id);
            request.setLimit(300);
            request.setOffset(0);

            var complaintList = wxService.getComplaintsService().queryNegotiationHistorys(request);

            if (complaintList != null)
                return JsonHelper.toJson(complaintList.getData());
        } catch (Exception e) {
            log.error("微信投诉--协商历史，获取异常，{}", id, e);
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
        String requestData = WxPayHtmlUtils.fetchRequest2Str(request);
        SignatureHeader header = WxPayHtmlUtils.fetchRequest2SignatureHeader(request);
        response.setHeader("content-type", "application/json;charset=UTF-8");

        TradeResult tradeResult = new TradeResult(PayTypeEnumd.WxComplain);

        try {
            var v3Result = wxService.parseComplaintNotifyResult(requestData, header);

            if (v3Result != null && v3Result.getResult() != null) {
                var rawData = v3Result.getRawData();
                var result = v3Result.getResult();
                var json = JsonHelper.toJson(rawData);
                var resultJson = JsonHelper.toJson(result);
                log.info("微信投诉--回调--微信返回：requestData:{},header:{},result:{},rawData:{}", requestData, JsonUtils.toJsonString(header), resultJson, json);


                var jsonObject = JSONObject.parseObject(json);
                var jsonObjectResutl = JSONObject.parseObject(resultJson);
                jsonObject.putAll(jsonObjectResutl);

                Boolean callFlag = callable.call(jsonObject.toJSONString());
                if (callFlag) {
                    response.getWriter().write(JsonUtils.toJsonString(WxPayResult.ok()));
                } else {
                    log.error("微信投诉--回调--错误：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header));
                    response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));
                }
            } else {
                log.error("微信投诉--回调--错误：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header));
                response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));
            }

        } catch (WxPayException e) {
            log.error("微信投诉--回调--异常：requestData:{},header:{}", requestData, JsonUtils.toJsonString(header), e);
            response.getWriter().write(JsonUtils.toJsonString(WxPayResult.error()));

        }
    }

    /**
     * 回复投诉
     *
     * @param id          投诉id
     * @param resp        回复内容
     * @param imgs        图片，json数组,传入调用商户上传反馈图片接口返回的media_id，最多上传4张图片凭证
     * @param jumpUrl     跳转链接
     * @param jumpUrlText 跳转链接文字
     */
    @SneakyThrows
    @Override
    public void response(String id, String resp, String imgs, String jumpUrl, String jumpUrlText) {
        try {
            ResponseRequest request = new ResponseRequest();
            request.setComplaintId(id);
            request.setComplaintedMchid(wxPayProperties.getMchId());
            request.setResponseContent(resp);
            request.setResponseImages(imgs);
            request.setJumpUrl(jumpUrl);
            request.setJumpUrlText(jumpUrlText);
            wxService.getComplaintsService().submitResponse(request);
        } catch (Exception e) {
            log.error("微信投诉--回复投诉，异常,id:{},resp:{}", id, resp, e);

        }

    }

    /**
     * 反馈处理完成
     *
     * @param id 投诉id
     * @return
     */
    @SneakyThrows
    @Override
    public Boolean complete(String id) {
        try {
            wxService.getComplaintsService().complete(new CompleteRequest(id, wxPayProperties.getMchId()));
            return true;
        } catch (Exception e) {
            log.error("微信投诉--回复投诉，异常,id:{}", id, e);
        }
        return false;
    }


    /**
     * 商户上传反馈图片
     *
     * @return
     */
    @SneakyThrows
    @Override
    public String uploadImg(InputStream inputStream, String fileName) {
        var imageUploadResult = wxService.getMerchantMediaService().imageUploadV3(inputStream, fileName);
        if (imageUploadResult != null && StringUtils.isNotBlank(imageUploadResult.getMediaId()))
            return imageUploadResult.getMediaId();
        return null;
    }

}
