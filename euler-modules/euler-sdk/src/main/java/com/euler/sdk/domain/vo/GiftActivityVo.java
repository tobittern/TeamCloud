package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 礼包活动管理视图对象 gift_activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("活动管理视图对象")
@ExcelIgnoreUnannotated
public class GiftActivityVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 面向游戏
     */
    @ExcelProperty(value = "面向游戏")
    @ApiModelProperty("面向游戏")
    private List<KeyValueDto> gameOriented;

    /**
     * 礼包名
     */
    @ExcelProperty(value = "礼包名")
    @ApiModelProperty("礼包名")
    private String giftName;

    /**
     * 礼包介绍
     */
    @ExcelProperty(value = "礼包介绍")
    @ApiModelProperty("礼包介绍")
    private String giftIntroduction;

    /**
     * 礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包
     */
    @ExcelProperty(value = "礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长 6新人礼包 7实名认证礼包", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "分=")
    @ApiModelProperty("礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包")
    private Integer giftType;

    /**
     * 礼包的一个icon
     */
    @ExcelProperty(value = "礼包的一个icon")
    @ApiModelProperty("礼包的一个icon")
    private String giftIcon;

    /**
     * 礼包的奖励条件
     */
    @ExcelProperty(value = "礼包的奖励条件")
    @ApiModelProperty("礼包的奖励条件")
    private Integer rewardConditions;

    /**
     * 礼包奖励类型 1 平台币 2余额  3积分
     */
    @ExcelProperty(value = "礼包奖励类型 1 平台币 2余额  3积分")
    @ApiModelProperty("礼包奖励类型 1 平台币 2余额  3积分")
    private Integer giftRewardType;

    /**
     * 礼包的奖励数量
     */
    @ExcelProperty(value = "礼包的奖励数量")
    @ApiModelProperty("礼包的奖励数量")
    private Integer giftRewardNums;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ApiModelProperty("排序")
    private String sort;

    /**
     * 是否上线 0上线 1下线
     */
    @ExcelProperty(value = "是否上线 0上线 1下线")
    @ApiModelProperty("是否上线 0上线 1下线")
    private Integer isOnline;

    /**
     * 是否可以领取 0不能 1可以 2已领取
     */
    @ExcelProperty(value = "是否可以领取 0不能 1可以 2已领取")
    @ApiModelProperty("是否可以领取 0不能 1可以 2已领取")
    private Integer isReceive = 0;


}
