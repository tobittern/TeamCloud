package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 搜索历史视图对象 history_search
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("搜索历史视图对象")
@ExcelIgnoreUnannotated
public class HistorySearchVo {

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
     * 搜索内容
     */
    @ExcelProperty(value = "搜索内容")
    @ApiModelProperty("搜索内容")
    private String key;

    /**
     * 对于key的搜索次数
     */
    @ExcelProperty(value = "对于key的搜索次数")
    @ApiModelProperty("对于key的搜索次数")
    private Integer num;

    /**
     * 搜索位置(0首页搜索,1发现页搜索)
     */
    @ExcelProperty(value = "搜索位置(0首页搜索,1发现页搜索)")
    @ApiModelProperty("搜索位置(0首页搜索,1发现页搜索)")
    private Integer position;

}
