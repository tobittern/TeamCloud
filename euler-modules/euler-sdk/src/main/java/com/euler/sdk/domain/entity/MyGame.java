package com.euler.sdk.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 我的游戏对象 my_game
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("my_game")
public class MyGame extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 渠道id
     */
    private Integer channelId;
    /**
     * 渠道名
     */
    private String channelName;
    /**
     * 分包的名称
     */
    private String packageCode;

    /**
     * 区服
     */
    private String regionalService;

    /**
     * 游戏等级
     */
    private String gameLevel;
    /**
     * 游戏图标
     */
    private String gameIcon;
    /**
     * 游戏名
     */
    private String gameName;
    /**
     * 上次游玩时间
     */
    private Date visitTime;
    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    private Integer platformServices;
    /**
     * 游戏的类型 1玩过  2公测 3预约
     */
    private Integer gameType;
    /**
     * 游戏时长，单位以小时显示，精确到小数点后1位
     */
    private Float gameDuration;
    /**
     * 显示游戏角色昵称
     */
    private String nickname;
    /**
     * 角色等级 (1：初级 2:中级 3:高级)
     */
    private Integer rank;
    /**
     * 开服时间
     */
    private Date  openingServiceTime;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
