package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态转发视图对象 dynamic_forward
 *
 * @author euler
 * @date 2022-06-20
 */
@Data
@ApiModel("动态转发视图对象")
@ExcelIgnoreUnannotated
public class DynamicForwardVo {

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
     * 转发用户id
     */
    @ExcelProperty(value = "转发用户id")
    @ApiModelProperty("转发用户id")
    private Long memberId;

}
