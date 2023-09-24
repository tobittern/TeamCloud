package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 游戏审核基础数据统计
 *
 * @author open
 * @date 2022-02-23
 */
@Data
@ApiModel("游戏审核基础数据统计")
@ExcelIgnoreUnannotated
public class OpenGameDataCountVo {

    private static final long serialVersionUID = 1L;

    /**
     * 申请中
     */
    @ExcelProperty(value = "申请中")
    @ApiModelProperty("申请中")
    private Integer inCount = 0;

    /**
     * 通过
     */
    @ExcelProperty(value = "通过")
    @ApiModelProperty("通过")
    private Integer passCount = 0;

    /**
     * 下架
     */
    @ExcelProperty(value = "下架")
    @ApiModelProperty("下架")
    private Integer downCount = 0;

    /**
     * 驳回
     */
    @ExcelProperty(value = "驳回")
    @ApiModelProperty("驳回")
    private Integer rejectCount = 0;

}
