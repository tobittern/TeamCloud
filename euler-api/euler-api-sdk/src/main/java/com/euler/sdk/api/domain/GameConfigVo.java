package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.system.api.domain.SysDictData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 游戏配置视图对象 game_config
 *
 * @author euler
 * @date 2023-03-23
 */
@Data
@ApiModel("游戏配置视图对象")
@ExcelIgnoreUnannotated
public class GameConfigVo implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 游戏Id
     */
    @ExcelProperty(value = "游戏Id")
    @ApiModelProperty("游戏Id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 运行平台 (1 安卓 2 ios 3 h5)
     */
    @ExcelProperty(value = "运行平台 (1 安卓 2 ios 3 h5)")
    @ApiModelProperty("运行平台 (1 安卓 2 ios 3 h5)")
    private String platform;

    /**
     * 配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播
     */
    @ExcelProperty(value = "配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播")
    @ApiModelProperty("配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播")
    private String type;

    /**
     * 游戏配置数据，格式为json格式
     */
    @ExcelProperty(value = "游戏配置数据，格式为json格式")
    @ApiModelProperty("游戏配置数据，格式为json格式")
    private String data;

    /**
     * SDK菜单列表(前端页面展示用)
     */
    @ApiModelProperty("SDK菜单列表")
    private List<GameConfigData> sdkMenuList;

    /**
     * SDK钱包菜单列表(前端页面展示用)
     */
    @ApiModelProperty("SDK钱包菜单列表")
    private List<GameConfigData> sdkWalletMenuList;

    /**
     * SDK虚拟钱包菜单列表(前端页面展示用)
     */
    @ApiModelProperty("SDK虚拟钱包菜单列表")
    private List<GameConfigData> sdkVirtualWalletMenuList;

    /**
     * 游戏支付方式列表(前端页面展示用)
     */
    @ApiModelProperty("游戏支付方式列表")
    private List<GameConfigData> gamePayTypeList;

    /**
     * 苹果应用类支付条件列表(前端页面展示用)
     */
    @ApiModelProperty("苹果应用类支付条件列表")
    private List<GameConfigData> appIosPaymentTermList;

    /**
     * 事件广播列表(前端页面展示用)
     */
    @ApiModelProperty("事件广播列表")
    private List<GameConfigData> eventBroadcastList;

}
