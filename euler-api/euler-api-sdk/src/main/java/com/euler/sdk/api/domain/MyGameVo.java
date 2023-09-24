package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 我的游戏视图对象 my_game
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("我的游戏视图对象")
@ExcelIgnoreUnannotated
public class MyGameVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 渠道id
     */
    @ExcelProperty(value = "渠道id")
    @ApiModelProperty(value = "渠道id", required = true)
    private Integer channelId;

    /**
     * 渠道名
     */
    @ExcelProperty(value = "渠道名")
    @ApiModelProperty(value = "渠道名", required = true)
    private String channelName;

    /**
     * 分包的名称
     */
    @ExcelProperty(value = "分包的名称")
    @ApiModelProperty(value = "分包的名称", required = true)
    private String packageCode;

    /**
     * 区服
     */
    @ExcelProperty(value = "区服")
    @ApiModelProperty(value = "区服", required = true)
    private String regionalService;


    /**
     * 游戏等级
     */
    @ExcelProperty(value = "游戏等级")
    @ApiModelProperty("游戏等级")
    private String gameLevel;

    /**
     * 游戏图标
     */
    @ExcelProperty(value = "游戏图标")
    @ApiModelProperty("游戏图标")
    private String gameIcon;

    /**
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;

    /**
     * 上次游玩时间
     */
    @ExcelProperty(value = "上次游玩时间")
    @ApiModelProperty("上次游玩时间")
    private Date visitTime;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ExcelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)", converter = ExcelDictConvert.class)
    @ApiModelProperty("运行平台 (1 安卓  2 ios  3 h5)")
    private Integer platformServices;

    /**
     * 游戏的类型 1玩过  2公测 3预约
     */
    @ExcelProperty(value = "游戏的类型 1玩过  2公测 3预约")
    @ApiModelProperty("游戏的类型 1玩过  2公测 3预约")
    private Integer gameType;

    /**
     * 游戏时长，单位以小时显示，精确到小数点后1位
     */
    @ExcelProperty(value = "游戏时长，单位以小时显示，精确到小数点后1位")
    @ApiModelProperty("游戏时长，单位以小时显示，精确到小数点后1位")
    private Float gameDuration;

    /**
     * 显示游戏角色昵称
     */
    @ExcelProperty(value = "显示游戏角色昵称")
    @ApiModelProperty("显示游戏角色昵称")
    private String nickname;

    /**
     * 角色等级 (1：初级 2:中级 3:高级)
     */
    @ExcelProperty(value = "角色等级 (1：初级 2:中级 3:高级)")
    @ApiModelProperty("角色等级 (1：初级 2:中级 3:高级)")
    private Integer rank;

    /**
     * 还有多少天开服
     */
    @ExcelProperty(value = "还有多少天开服")
    @ApiModelProperty("还有多少天开服")
    private String serviceTimeDay;

    /**
     * 开服时间
     */
    @ExcelProperty(value = "开服时间")
    @ApiModelProperty("开服时间")
    private Date openingServiceTime;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 多少天前玩过
     */
    @ExcelProperty(value = "多少天前玩过")
    @ApiModelProperty("多少天前玩过")
    private String howDaysAgo = "";


    /**
     * 账号状态 0正常  1封禁
     */
    @ExcelProperty(value = "账号状态")
    @ApiModelProperty("账号状态")
    private Integer memberStatus = 0;

    /**
     * 封号截止时间
     */
    @ExcelProperty(value = "封号截止时间")
    @ApiModelProperty("封号截止时间")
    private Date blackTime;

    /**
     * 是否是永久封号  0 不是  1是
     */
    @ExcelProperty(value = "是否是永久封号  0 不是  1是")
    @ApiModelProperty("是否是永久封号  0 不是  1是")
    private Integer isPermanent;


    /**
     * gameInstallPackage
     */
    @ExcelProperty(value = "gameInstallPackage")
    @ApiModelProperty("gameInstallPackage")
    private String gameInstallPackage;

    /**
     * packageName
     */
    @ExcelProperty(value = "packageName")
    @ApiModelProperty("packageName")
    private String packageName;

    /**
     * universalLink
     */
    @ExcelProperty(value = "universalLink")
    @ApiModelProperty("universalLink")
    private String universalLink;

    /**
     * urlScheme
     */
    @ExcelProperty(value = "urlScheme")
    @ApiModelProperty("urlScheme")
    private String urlScheme;

}
