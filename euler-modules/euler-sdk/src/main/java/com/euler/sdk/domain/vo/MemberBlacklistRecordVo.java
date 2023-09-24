package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 黑名单封禁操作记录视图对象 member_blacklist_record
 *
 * @author euler
 * @date 2022-06-16
 */
@Data
@ApiModel("黑名单封禁操作记录视图对象")
@ExcelIgnoreUnannotated
public class MemberBlacklistRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ExcelProperty(value = "自增主键")
    @ApiModelProperty("自增主键")
    private Integer id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long memberId;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    @ApiModelProperty("描述")
    private String description;

    /**
     * 封号截止时间
     */
    @ExcelProperty(value = "封号截止时间")
    @ApiModelProperty("封号截止时间")
    private String endTime;

    /**
     * 封号原因
     */
    @ExcelProperty(value = "封号原因")
    @ApiModelProperty("封号原因")
    private String record;

    /**
     * 操作类型 1封号 2解封
     */
    @ExcelProperty(value = "操作类型 1封号 2解封")
    @ApiModelProperty("操作类型 1封号 2解封")
    private Integer type;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    @ApiModelProperty("创建者")
    private String createBy;


    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;



}
