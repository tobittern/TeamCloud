package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 成长体系对象 growth_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("growth_system")
public class GrowthSystem extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 游戏用户id
     */
    private Long userId;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    private Integer gradeType;

    /**
     * 成长等级(1~10)
     */
    private Integer growthGrade;

    /**
     * 一元能够兑换的成长值
     */
    private Integer growthValue;

    /**
     * 用户充值的钱
     */
    private Long money;

    /**
     * 充值所兑换的成长值
     */
    private Long moneyGrowthValue;

    /**
     * 总成长值
     */
    private Long growthTotal;

    /**
     * 删除状态(0 正常  1删除)
     */
    @TableLogic
    private String delFlag;

}
