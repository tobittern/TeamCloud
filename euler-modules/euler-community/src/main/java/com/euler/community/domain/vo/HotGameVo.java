package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 视图对象 hot_game
 *
 * @author euler
 * @date 2022-06-17
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class HotGameVo {

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
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Long gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 游戏图片
     */
    @ExcelProperty(value = "游戏图片")
    @ApiModelProperty("游戏图片")
    private String gamePic;

    /**
     * 查看游戏次数
     */
    @ExcelProperty(value = "查看游戏次数")
    @ApiModelProperty("查看游戏次数")
    private Integer num;

    /**
     * 平台
     */
    @ExcelProperty(value = "平台")
    @ApiModelProperty("平台")
    private Integer operationPlatform;

}
