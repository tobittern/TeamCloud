package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 举报业务对象 report
 *
 * @author euler
 * @date 2022-06-09
 */

@Data
@ApiModel("举报业务对象")
public class ReportDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 关联的id
     */
    @ApiModelProperty(value = "关联的id")
    @NotNull(message = "关联的ID不能为空")
    private Long relationId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 举报类型 1动态 2评论
     */
    @ApiModelProperty(value = "举报类型 1动态 2评论")
    @NotBlank(message = "举报类型 1动态 2评论不能为空")
    private String type;

    /**
     * 所属动态的id
     */
    @ApiModelProperty(value = "关联的id")
    private Long dynamicId;

}
