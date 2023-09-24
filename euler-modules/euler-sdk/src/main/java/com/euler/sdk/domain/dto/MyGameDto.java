package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的游戏业务对象 my_game
 *
 * @author euler
 * @date 2022-03-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("我的游戏业务对象")
public class MyGameDto extends PageQuery {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    private Integer gameId;

    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道id", required = true)
    private Integer channelId;

    /**
     * 分包的名称
     */
    @ApiModelProperty(value = "分包的名称", required = true)
    private String packageCode;

    /**
     * 区服
     */
    @ApiModelProperty(value = "区服", required = true)
    private String regionalService;

    /**
     * 游戏名
     */
    @ApiModelProperty(value = "游戏名", required = true)
    private String gameName;


    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)", required = true)
    private Integer platformServices;

    /**
     * 游戏的类型 1玩过  2公测 3预约
     */
    @ApiModelProperty(value = "游戏的类型 1玩过  2公测 3预约", required = true)
    private Integer gameType;


    /**
     * 显示游戏角色昵称
     */
    @ApiModelProperty(value = "显示游戏角色昵称", required = true)
    private String nickname;

    /**
     * 角色等级 (1：初级 2:中级 3:高级)
     */
    @ApiModelProperty(value = "角色等级 (1：初级 2:中级 3:高级)", required = true)
    private Integer rank;


}

