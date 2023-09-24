package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 弹窗管理视图对象 popup
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@ApiModel("弹窗管理视图对象")
@ExcelIgnoreUnannotated
public class SdkPopupVo implements Serializable {

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
    private Long memberId;

    /**
     * 弹框名
     */
    @ExcelProperty(value = "弹框名")
    @ApiModelProperty("弹框名")
    private String title;

    /**
     * 弹框类型 1强退  2奖励 3运营
     */
    @ExcelProperty(value = "弹框类型 1强退  2奖励 3运营 ")
    @ApiModelProperty("弹框类型 1强退  2奖励 3运营 ")
    private Integer type;

    /**
     * 每天显示次数 大约999就代表着一直持续
     */
    @ExcelProperty(value = "每天显示次数 大约999就代表着一直持续")
    @ApiModelProperty("每天显示次数 大约999就代表着一直持续")
    private Integer times;

    /**
     * 弹窗显示开始时间
     */
    @ExcelProperty(value = "弹窗显示开始时间")
    @ApiModelProperty("弹窗显示开始时间")
    private Date startTime;

    /**
     * 弹窗显示结束时间
     */
    @ExcelProperty(value = "弹窗显示结束时间")
    @ApiModelProperty("弹窗显示结束时间")
    private Date endTime;

    /**
     * 显示时间
     */
    @ExcelProperty(value = "显示时间")
    @ApiModelProperty("显示时间")
    private String showTime;

    /**
     * 展示类型 1图片 2文本
     */
    @ApiModelProperty(value = "展示类型 1图片 2文本", required = true)
    private Integer showType;

    /**
     * 图片横
     */
    @ExcelProperty(value = "图片横")
    @ApiModelProperty("图片横")
    private String pictureTransverse;

    /**
     * 图片纵
     */
    @ExcelProperty(value = "图片纵")
    @ApiModelProperty("图片纵")
    private String pictureLongitudinal;

    /**
     * 展示内容
     */
    @ExcelProperty(value = "展示内容")
    @ApiModelProperty("展示内容")
    private String showContent;

    /**
     * 跳转url
     */
    @ExcelProperty(value = "跳转url")
    @ApiModelProperty("跳转url")
    private String jumpUrl;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ExcelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高")
    @ApiModelProperty("显示优先级，默认值0，数字越小，显示级别越高")
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    @ExcelProperty(value = "弹窗状态，0待启用，1已启用，2已停用")
    @ApiModelProperty("弹窗状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 游戏IDS
     */
    @ExcelProperty(value = "游戏IDS")
    @ApiModelProperty("游戏IDS")
    private List<Integer> gameIds;

    /**
     * 礼包ID
     */
    @ExcelProperty(value = "礼包ID")
    @ApiModelProperty("礼包ID")
    private Integer giftBagId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
     */
    @ExcelProperty(value = "启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)")
    @ApiModelProperty("启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)")
    private String startOccasion;

    /**
     * 每次启动类型(0:打开App 1:进入游戏)
     */
    @ExcelProperty(value = "每次启动类型(0:打开App 1:进入游戏)")
    @ApiModelProperty("每次启动类型(0:打开App 1:进入游戏)")
    private String everyStartupType;

    /**
     * 弹窗时间，单位：秒
     */
    @ExcelProperty(value = "弹窗时间，单位：秒")
    @ApiModelProperty("弹窗时间，单位：秒")
    private Integer popupTime;

    /**
     * 角色注册时间，单位：天，-1默认不限制
     */
    @ExcelProperty(value = "角色注册时间，单位：天")
    @ApiModelProperty("角色注册时间，单位：天")
    private Integer roleRegistTime;

    /**
     * 满足条件的值(启动时机选择【2:游戏到达等级 3:累计充值 4:累计在线时长(分)】时需要要填)
     */
    @ExcelProperty(value = "满足条件的值")
    @ApiModelProperty("满足条件的值")
    private String conditionValue;

}
