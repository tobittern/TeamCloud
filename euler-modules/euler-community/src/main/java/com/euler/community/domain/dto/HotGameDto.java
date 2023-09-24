package com.euler.community.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.euler.common.mybatis.core.page.PageQuery;
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
public class HotGameDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private Long gameId;


    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 游戏图片
     */
    @ApiModelProperty("游戏图片")
    private String gamePic;

    /**
     * 查看游戏次数
     */
    @ApiModelProperty("查看游戏次数")
    private Integer num;

    /**
     * 平台
     */
    @ApiModelProperty("平台")
    private Integer operationPlatform;

    /**
     * 主键id 集合
     */
    private Long[] ids;

}
