package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 点赞视图对象 praise
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("点赞视图对象")
@ExcelIgnoreUnannotated
public class PraiseVo {

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
    private Long relationId;

    /**
     * 点赞用户id
     */
    @ExcelProperty(value = "点赞用户id")
    @ApiModelProperty("点赞用户id")
    private Long memberId;

}
