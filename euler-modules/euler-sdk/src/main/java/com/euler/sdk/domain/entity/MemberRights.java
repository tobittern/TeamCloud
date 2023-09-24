package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员权益对象 member_rights
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_rights")
public class MemberRights extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员等级 1:初级 2:中级 3:高级
     */
    private String lever;

    /**
     * 会员状态：1:生效中 2:已失效
     */
    private String status;

    /**
     * 是否升级：0:未升级 1:升级
     */
    private String isUpgrade;


    /**
     * 会员时长
     */
    private String memberDuration;

    /**
     * 有效期开始时间
     */
    private Date validateStartTime;

    /**
     * 有效期结束时间
     */
    private Date validateEndTime;

    /**
     * 上传的url
     */
    private String uploadUrl;

    /**
     * 上传的文件名
     */
    private String uploadFileName;

    /**
     * 上传的文件后缀名
     */
    private String uploadFileSuffix;

    /**
     * 上传状态 0:未上传 1:已上传
     */
    private String uploadStatus;

    /**
     * 删除状态(0 正常  1删除)
     */
    @TableLogic
    private String delFlag;

    /**
     * 商品id
     */
    private  Integer goodsId;

    /**
     * 会员权益价格
     */
    private BigDecimal goodsPrice;

    /**
     * 售价
     */
    private BigDecimal payPrice;

}
