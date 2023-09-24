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
 * 动态转发业务对象 dynamic_forward
 *
 * @author euler
 * @date 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("动态转发业务对象")
public class DynamicForwardBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 动态表主键id
     */
    @ApiModelProperty(value = "动态表主键id", required = true)
    @NotNull(message = "动态表主键id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long dynamicId;

    /**
     * 转发用户id
     */
    @ApiModelProperty(value = "转发用户id")
    private Long memberId;

}
