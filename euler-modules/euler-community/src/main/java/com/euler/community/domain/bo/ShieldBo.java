package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;



/**
 * 屏蔽信息业务对象 shield
 *
 * @author euler
 * @date 2022-09-15
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("屏蔽信息业务对象")
public class ShieldBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id", required = true)
    @NotNull(message = "屏蔽信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long businessId;

    /**
     * 业务类型，1：用户：2动态
     */
    @ApiModelProperty(value = "业务类型，1：用户：2动态", required = true)
    @NotNull(message = "屏蔽类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer businessType;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因", required = true)
    private String reason;


}
