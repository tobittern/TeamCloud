package com.euler.payment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户与商家之间的协商记录对象 ali_complaint_reply_detail_infos
 *
 * @author euler
 * @date 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ali_complaint_reply_detail_infos")
public class AliComplaintReplyDetailInfos extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 支付宝侧投诉单号
     */
    private String complainEventId;
    /**
     * 回复人名称
     */
    private String replierName;
    /**
     * 用户角色
     */
    private String replierRole;
    /**
     * 回复时间
     */
    private String gmtCreate;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复图片
     */
    private String imagesString;

    /**
     * 删除状态
     */
    @TableLogic
    private String delFlag;
}
