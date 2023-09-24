package com.euler.sdk.api.domain;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 积分体系业务对象 score_system
 *
 * @author euler
 * @date 2022-03-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("积分体系业务对象")
public class ScoreSystemBo extends BaseEntity {

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
     * 积分
     */
    @ApiModelProperty(value = "积分", required = true)
    @NotNull(message = "积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long score;

    /**
     * 类型 0:签到积分 1:首次注册积分
     */
    @ApiModelProperty(value = "类型 0:签到积分 1:首次注册积分", required = true)
    @NotBlank(message = "类型 0:签到积分 1:首次注册积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    private String desc;

}
