package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 动态所有资源业务对象 resource
 *
 * @author euler
 * @date 2022-06-09
 */

@Data
@ApiModel("动态所有资源业务对象")
public class ResourceBo {

    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id", required = true)
    private Long memberId;

    /**
     * 动态表主键id
     */
    @ApiModelProperty(value = "动态表主键id", required = true)
    @NotNull(message = "动态表主键id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long dynamicId;

    /**
     * 附件路径 可能存在多个
     */
    @ApiModelProperty(value = "附件路径", required = true)
    @NotBlank(message = "附件路径不能为空", groups = {AddGroup.class, EditGroup.class})
    private String filePath;

}
