package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 举报视图对象 report
 *
 * @author euler
 * @date 2022-06-09
 */
@Data
@ApiModel("举报视图对象")
@ExcelIgnoreUnannotated
public class ReportVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 关联的ID
     */
    @ExcelProperty(value = "关联的ID")
    @ApiModelProperty("关联的ID")
    private Long relationId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 举报类型 1动态 2评论
     */
    @ExcelProperty(value = "举报类型 1动态 2评论")
    @ApiModelProperty("举报类型 1动态 2评论")
    private String type;

    /**
     * 举报理由
     */
    @ExcelProperty(value = "举报理由")
    @ApiModelProperty("举报理由")
    private String reason;

    /**
     * 举报人
     */
    @ExcelProperty(value = "举报人")
    @ApiModelProperty("举报人")
    private String reportUserName;

    /**
     * 举报时间
     */
    @ExcelProperty(value = "举报时间")
    @ApiModelProperty("举报时间")
    private Date reportTime;

    /**
     * 评论人
     */
    @ExcelProperty(value = "评论人")
    @ApiModelProperty("评论人")
    private String commentsUserName;

    /**
     * 评论内容
     */
    @ExcelProperty(value = "评论内容")
    @ApiModelProperty("评论内容")
    private String commentsContent;

    /**
     * 所属动态的id
     */
    @ExcelProperty(value = "所属动态的id")
    @ApiModelProperty("所属动态的id")
    private Long dynamicId;

}
