package com.euler.community.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 审核记录业务对象 dynamic_audit_record
 *
 * @author euler
 * @date 2022-06-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("审核记录业务对象")
public class DynamicAuditRecordBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id", required = true)
    @NotNull(message = "动态id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long dynamicId;

    /**
     * 审核人id
     */
    @ApiModelProperty(value = "审核人id", required = true)
    @NotNull(message = "审核人id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long auditId;

    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    @ApiModelProperty(value = "审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝", required = true)
    @NotNull(message = "审核状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer auditStatus;
    /**
     * 审核原因
     */
    @ApiModelProperty("审核原因")
    private String auditContent;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间", required = true)
    @NotNull(message = "审核时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date auditTime;


}
