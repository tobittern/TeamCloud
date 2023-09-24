package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 积分体系视图对象 score_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("积分体系视图对象")
@ExcelIgnoreUnannotated
public class ScoreSystemVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 游戏用户id
     */
    @ExcelProperty(value = "游戏用户id")
    @ApiModelProperty("游戏用户id")
    private Long userId;

    /**
     * 积分
     */
    @ExcelProperty(value = "积分")
    @ApiModelProperty("积分")
    private Long score;

    /**
     * 类型 0:签到积分 1:首次注册积分
     */
    @ApiModelProperty(value = "类型 0:签到积分 1:首次注册积分")
    private String type;

}
