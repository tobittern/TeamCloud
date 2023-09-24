package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 成长值配置对象 growth_config
 *
 * @author euler
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("growth_config")
public class GrowthConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    private Integer type;

    /**
     * 成长等级(1~10)
     */
    private Integer grade;

    /**
     * 梯度
     */
    private Integer gradient;

    /**
     * 升级所需要的成长值
     */
    private Long upgradeValue;

    /**
     * 删除状态(0 正常  1删除)
     */
    @TableLogic
    private String delFlag;

}
