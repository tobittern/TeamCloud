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
 * 成长值配置业务对象 growth_config
 *
 * @author euler
 * @date 2022-03-25
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("成长值配置业务对象")
public class GrowthConfigBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称", required = true)
    @NotBlank(message = "等级名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    @ApiModelProperty(value = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊", required = true)
    @NotNull(message = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer type;

    /**
     * 成长等级(1~10)
     */
    @ApiModelProperty(value = "成长等级(1~10)", required = true)
    @NotNull(message = "成长等级(1~10)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer grade;

    /**
     * 梯度
     */
    @ApiModelProperty(value = "梯度", required = true)
    @NotNull(message = "梯度不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer gradient;

    /**
     * 升级所需要的成长值
     */
    @ApiModelProperty(value = "升级所需要的成长值", required = true)
    @NotNull(message = "升级所需要的成长值不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long upgradeValue;

}
