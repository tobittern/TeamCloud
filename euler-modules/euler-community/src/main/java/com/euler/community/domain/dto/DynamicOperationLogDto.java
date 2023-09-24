package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 动态操作错误日志业务对象 dynamic_operation_log
 *
 * @author euler
 * @date 2022-06-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("动态操作错误日志业务对象")
public class DynamicOperationLogDto extends PageQuery {


    /**
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID", required = true)
    private Long memberId;

    /**
     * 动态id
     */
    @ApiModelProperty(value = "动态id", required = true)
    private Long dynamicId;

    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型", required = true)
    private Integer operationType;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    private String operationContent;


}
