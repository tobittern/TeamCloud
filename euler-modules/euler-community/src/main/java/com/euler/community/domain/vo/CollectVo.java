package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态收藏视图对象 collect
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("动态收藏视图对象")
@ExcelIgnoreUnannotated
public class CollectVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 动态表主键id
     */
    @ExcelProperty(value = "动态表主键id")
    @ApiModelProperty("动态表主键id")
    private Long dynamicId;

    /**
     * 收藏用户id
     */
    @ExcelProperty(value = "收藏用户id")
    @ApiModelProperty("收藏用户id")
    private Long memberId;

    /**
     * 收藏状态 1 收藏中  2取消收藏
     */
    @ExcelProperty(value = "收藏状态 1 收藏中  2取消收藏")
    @ApiModelProperty("收藏状态 1 收藏中  2取消收藏")
    private String status;


}
