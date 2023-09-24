package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;


/**
 * 投诉详情视图对象 complaint_info
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉详情视图对象")
@ExcelIgnoreUnannotated
public class AliComplaintInfoVo extends AliComplaintVo {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 应用id，例如小程序id、生活号id、商家pid
     */
    private String targetId;

    /**
     * 应用类型 小程序为：APPID
     * 生活号为：PUBLICID
     * 商家为：PID
     */
    private String targetType;

    /**
     * 支付宝侧投诉单号
     */
    private String complainEventId;

    /**
     * 状态
     * 商家处理中：MERCHANT_PROCESSING
     * 商家已反馈：MERCHANT_FEEDBACKED
     * 投诉已完结：FINISHED
     * 投诉已撤销：CANCELLED
     * 平台处理中：PLATFORM_PROCESSING
     * 平台处理完结：PLATFORM_FINISH
     * 系统关闭：CLOSED
     */
    private String status;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 	商家订单号
     */
    private String merchantOrderNo;

    /**
     * 投诉单创建时间
     */
    private String gmtCreate;

    /**
     * 诉单修改时间
     */
    private String gmtModified;

    /**
     * 投诉单结束时间
     */
    private String gmtFinished;

    /**
     * 投诉诉求
     */
    private String  leafCategoryName;

    /**
     * 投诉原因
     */
    private String complainReason;

    /**
     * 投诉内容
     */
    private String content;

    /**
     * 投诉图片
     */
    private List<String> images;

    /**
     * 投诉图片
     */
    private String imagesString;

    /**
     * 投诉人电话号
     */
    private String phoneNo;

    /**
     * 交易金额，单位元
     */
    private String tradeAmount;

    /**
     * 用户与商家之间的协商记录
     */
    private List<AliComplaintReplyDetailInfosVo> replyDetailInfos;


}
