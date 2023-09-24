package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 成长体系业务对象 growth_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("成长体系业务对象")
public class GrowthSystemBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 游戏用户id
     */
    @ApiModelProperty(value = "游戏用户id", required = true)
    @NotNull(message = "游戏用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    @ApiModelProperty(value = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊", required = true)
    @NotNull(message = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer gradeType;

    /**
     * 成长等级(1~10)
     */
    @ApiModelProperty(value = "成长等级(1~10)", required = true)
    @NotNull(message = "成长等级(1~10)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer growthGrade;

    /**
     * 一元能够兑换的成长值
     */
    @ApiModelProperty(value = "一元能够兑换的成长值", required = true)
    private Integer growthValue;

    /**
     * 用户充值的钱
     */
    @ApiModelProperty(value = "用户充值的钱", required = true)
    @NotNull(message = "用户充值的钱不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long money;

    /**
     * 充值所兑换的成长值
     */
    @ApiModelProperty(value = "充值所兑换的成长值", required = true)
    private Long moneyGrowthValue;

    /**
     * 总成长值
     */
    @ApiModelProperty(value = "总成长值", required = true)
    private Long growthTotal;

}
