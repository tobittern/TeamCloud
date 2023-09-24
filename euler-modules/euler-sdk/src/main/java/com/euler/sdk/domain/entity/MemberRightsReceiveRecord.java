package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员权益领取记录对象 member_rights_receive_record
 *
 * @author euler
 * @date 2022-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_rights_receive_record")
public class MemberRightsReceiveRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分
     */
    private String type;

    /**
     * 当前年份
     */
    private Integer currentYear;

    /**
     * 当前月份
     */
    private Integer currentMonth;

    /**
     * 当月领取平台币
     */
    private Integer receivePlatformCurrency;

    /**
     * 领取的积分
     */
    private Integer receiveScore;

    /**
     * 删除状态 0不删除 1删除
     */
    @TableLogic
    private String delFlag;

}
