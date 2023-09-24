package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 动态操作错误日志业务对象 dynamic_operation_error_log
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("动态操作错误日志业务对象")
public class DynamicOperationErrorLogBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id", required = true)
    @NotNull(message = "动态id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long dynamicId;

    /**
     * 操作类型  1 数据入es报错
     */
    @ApiModelProperty(value = "操作类型  1 数据入es报错", required = true)
    @NotNull(message = "操作类型  1 数据入es报错不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer operationType;


}
