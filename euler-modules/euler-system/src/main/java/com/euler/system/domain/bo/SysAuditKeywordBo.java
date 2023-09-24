package com.euler.system.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 审核关键词 - 敏感词业务对象 audit_keyword
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("审核关键词 - 敏感词业务对象")
public class SysAuditKeywordBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 添加用户id
     */
    @ApiModelProperty(value = "添加用户id", required = true)
    private Long userId;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    private Integer type = 1;

    /**
     * 敏感词
     */
    @ApiModelProperty(value = "敏感词", required = true)
    @NotBlank(message = "敏感词不能为空", groups = { AddGroup.class, EditGroup.class })
    private String keywords;


}
