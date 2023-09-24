package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审核记录业务对象 dynamic_audit_record
 *
 * @author euler
 * @date 2022-06-01
 */

@Data
@ApiModel("审核记录业务对象")
public class DynamicAuditRecordDto extends PageQuery {

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id", required = true)
    private Long dynamicId;

    /**
     * 审核人id
     */
    @ApiModelProperty(value = "审核人id", required = true)
    private Long auditId;

    /**
     * 审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝
     */
    @ApiModelProperty(value = "审核状态 0 初始状态 1 待审中  2审核成功 3审核拒绝", required = true)
    private Integer auditStatus;


}
