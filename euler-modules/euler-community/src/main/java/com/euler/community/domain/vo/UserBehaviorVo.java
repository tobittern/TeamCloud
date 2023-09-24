package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 用户行为记录视图对象 user_behavior
 *
 * @author euler
 * @date 2022-07-14
 */
@Data
@ApiModel("用户行为记录视图对象")
@ExcelIgnoreUnannotated
public class UserBehaviorVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    @ApiModelProperty("类型")
    private Integer type;

    /**
     * 关联ID
     */
    @ExcelProperty(value = "关联ID")
    @ApiModelProperty("关联ID")
    private Long relationId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private String time;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    @ApiModelProperty("更新时间")
    private Date updateTime;



}
