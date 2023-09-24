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
 * 签到配置业务对象 sign_config
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("签到配置业务对象")
public class SignConfigBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer id;

    /**
     * 分数
     */
    @ApiModelProperty(value = "分数", required = true)
    @NotNull(message = "分数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer score;


}
