package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 使用使用到不通地方的记录对象 game_use_record
 *
 * @author euler
 * @date 2022-03-30
 */
@Data
@TableName("game_use_record")
public class GameUseRecord {

    private static final long serialVersionUID = 1L;

    /**
     * 甲方ID 代表着是使用方
     */
    private Integer partyAId;
    /**
     * 乙方ID 被使用的ID
     */
    private Integer partyBId;
    /**
     * 类型 1等级礼包 2活动礼包 3活动创建
     */
    private Integer type;


}
