package com.euler.common.payment.core;

import com.alipay.api.FileItem;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.TableDataInfoCoreDto;
import com.euler.common.payment.bean.AliComplaintQueryDto;
import com.euler.common.payment.bean.ComplainCallable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 投诉处理
 */
public interface IAliComplaintService {

    /**
     * 投诉详情
     *
     * @param id 投诉id
     * @return
     */
    String getComplainInfo(String id);

    /**
     * 查询投诉分页列表
     *
     * @param complaintQueryDto
     * @return
     */
    String getComplainList(AliComplaintQueryDto complaintQueryDto);


    /**
     * 投诉回调
     *
     * @param request
     * @param response
     * @param callable
     */
    void AsyncNotify(HttpServletRequest request, HttpServletResponse response, ComplainCallable<Boolean> callable);

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
    R feedback(String id, String feedback_code, String feedback_content, String feedback_images, String operator);

    /**
     * 商家留言回复
     *
     * @param id            投诉id
     * @param reply_content 回复留言内容
     * @param reply_images  商家处理问题反馈时的回复凭证的图片id，多个逗号隔开（图片id可以通过"商户上传处理图片"接口获取）
     * @return
     */
    R reply(String id, String reply_content, String reply_images);

    /**
     * 商户上传处理图片
     *
     * @return
     */
    R<String> uploadImg(FileItem image_content, String image_type);

}
