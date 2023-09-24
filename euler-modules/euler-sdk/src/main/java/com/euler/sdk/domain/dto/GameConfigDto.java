package com.euler.sdk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;

/**
 * 游戏配置分页业务对象 game_config
 *
 * @author euler
 * @date 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("游戏配置分页业务对象")
public class GameConfigDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏Id
     */
    @ApiModelProperty(value = "游戏Id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    private String platform;

    /**
     * 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播
     */
    @ApiModelProperty(value = "配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播")
    private String type;

}
