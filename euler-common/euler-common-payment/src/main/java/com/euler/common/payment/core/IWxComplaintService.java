package com.euler.common.payment.core;

import com.euler.common.payment.bean.ComplainCallable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * 投诉处理
 */
public interface IWxComplaintService {

    /**
     * 创建投诉回调地址
     *
     * @param url 回调地址
     * @return
     */
    Boolean createNotifyUrl(String url);

    /**
     * 更新投诉回调地址
     *
     * @param url 回调地址
     * @return
     */
    Boolean updateNotifyUrl(String url);

    /**
     * 删除投诉回调地址
     *
     * @param url 回调地址
     * @return
     */
    Boolean deleteNotifyUrl(String url);

    /**
     * 查询投诉回调地址
     *
     * @return
     */
    String queryNotifyUrl();

    /**
     * 投诉详情
     *
     * @param id 投诉id
     * @return
     */
    String getComplainInfo(String id);

    /**
     * 协商历史
     *
     * @param id 投诉id
     * @return
     */
    String getNegotiationHistoryList(String id);

    /**
     * 投诉回调
     *
     * @param request
     * @param response
     * @param callable
     */
    void AsyncNotify(HttpServletRequest request, HttpServletResponse response, ComplainCallable<Boolean> callable);

    /**
     * 回复投诉
     *
     * @param id          投诉id
     * @param resp        回复内容
     * @param imgs        图片，json数组 传入调用商户上传反馈图片接口返回的media_id，最多上传4张图片凭证
     * @param jumpUrl     跳转链接
     * @param jumpUrlText 跳转链接文字
     */
    void response(String id, String resp, String imgs, String jumpUrl, String jumpUrlText);

    /**
     * 反馈处理完成
     *
     * @param id 投诉id
     * @return
     */
    Boolean complete(String id);

    /**
     * 商户上传反馈图片
     *
     * @return
     */
    String uploadImg(InputStream inputStream,String fileName);


}
