package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 礼包SDk数据对象 gift_bag_cdk
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gift_bag_cdk")
public class GiftBagCdk extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 礼包表id
     */
    private Long giftBagId;
    /**
     * 游戏id
     */
    private Long gameId;
    /**
     * 领取礼包的用户id
     */
    private Long memberId;
    /**
     * 礼包码
     */
    private String code;
    /**
     * 礼包状态，0：未使用，1：已使用
     */
    private Integer status;
    /**
     * 领取时间
     */
    private Date receiveTime;
    /**
     * 兑换时间
     */
    private Date exchangeTime;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
