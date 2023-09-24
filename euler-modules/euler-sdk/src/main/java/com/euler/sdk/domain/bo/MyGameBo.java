package com.euler.sdk.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 我的游戏业务对象 my_game
 *
 * @author euler
 * @date 2022-03-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("我的游戏业务对象")
public class MyGameBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "游戏id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer gameId;

    /**
     * 渠道id
     */
    @ExcelProperty(value = "渠道id")
    @NotNull(message = "渠道id", groups = { AddGroup.class, EditGroup.class })
    private Integer channelId;

    /**
     * 渠道名
     */
    @ExcelProperty(value = "渠道名")
    @NotNull(message = "渠道名", groups = { AddGroup.class, EditGroup.class })
    private String channelName;

    /**
     * 分包的名称
     */
    @ExcelProperty(value = "分包的名称")
    private String packageCode;

    /**
     * 区服
     */
    @ExcelProperty(value = "区服")
    @NotNull(message = "区服", groups = { AddGroup.class, EditGroup.class })
    private String regionalService;

    /**
     * 游戏等级
     */
    @ApiModelProperty(value = "游戏等级", required = true)
    @NotBlank(message = "游戏等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameLevel;
    /**
     * 游戏图标
     */
    @ApiModelProperty(value = "游戏图标", required = true)
    @NotBlank(message = "游戏图标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameIcon;

    /**
     * 游戏名
     */
    @ApiModelProperty(value = "游戏名", required = true)
    @NotBlank(message = "游戏名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameName;

    /**
     * 上次游玩时间
     */
    @ApiModelProperty(value = "上次游玩时间", required = true)
    @NotNull(message = "上次游玩时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date visitTime;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)", required = true)
    private Integer platformServices = 1;

    /**
     * 游戏的类型 1玩过  2公测 3预约
     */
    @ApiModelProperty(value = "游戏的类型 1玩过  2公测 3预约", required = true)
    private Integer gameType = 1;

    /**
     * 游戏时长，单位以小时显示，精确到小数点后1位
     */
    @ApiModelProperty(value = "游戏时长，单位以小时显示，精确到小数点后1位", required = true)
    private Float gameDuration;

    /**
     * 显示游戏角色昵称
     */
    @ApiModelProperty(value = "显示游戏角色昵称", required = true)
    @NotBlank(message = "显示游戏角色昵称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nickname;

    /**
     * 角色等级 (1：初级 2:中级 3:高级)
     */
    @ApiModelProperty(value = "角色等级 (1：初级 2:中级 3:高级)", required = true)
    private Integer rank = 1;


    /**
     * 开服时间
     */
    @ApiModelProperty(value = "开服时间", required = true)
    @NotNull(message = "开服时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date openingServiceTime;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;


}
