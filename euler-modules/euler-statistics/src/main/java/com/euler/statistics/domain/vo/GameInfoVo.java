package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("游戏用户管理视图对象")
@ExcelIgnoreUnannotated
public class GameInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 游戏名
     */
    @ApiModelProperty("游戏名")
    private String gameName;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 游戏图标
     */
    @ApiModelProperty("游戏图标")
    private String iconUrl;

}
