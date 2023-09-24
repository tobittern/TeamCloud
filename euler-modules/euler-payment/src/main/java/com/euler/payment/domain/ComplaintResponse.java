package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 投诉回复对象 complaint_response
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("complaint_response")
public class ComplaintResponse extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 投诉单号
     */
    private String complaintId;
    /**
     * 回复内容
     */
    private String responseContent;
    /**
     * 回复图片 传输给微信的是一个数组
     */
    private String responseImages;
    /**
     * 跳转链接
     */
    private String jumpUrl;
    /**
     * 跳转链接文案
     */
    private String jumpUrlText;
    /**
     * 删除状态 0 正常 2删除
     */
     @TableLogic
    private String delFlag;

}
