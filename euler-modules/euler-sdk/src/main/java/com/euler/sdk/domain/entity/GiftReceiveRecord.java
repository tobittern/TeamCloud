package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 礼包领取记录对象 gift_receive_record
 *
 * @author euler
 * @date 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gift_receive_record")
public class GiftReceiveRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 礼包类型 1:等级礼包 2:活动礼包
     */
    private String giftType;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 领取状态 0:未达成 1:待领取 2:已领取
     */
    private String receiveStatus;

    /**
     * 礼包id
     */
    private Integer giftId;

    /**
     * 删除状态 0正常 2删除
     */
    @TableLogic
    private String delFlag;

}
