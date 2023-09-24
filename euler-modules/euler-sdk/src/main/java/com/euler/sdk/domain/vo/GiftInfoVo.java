package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 礼包视图对象 gift_info
 *
 * @author euler
 * @date 2022-03-24
 */
@Data
@ApiModel("礼包视图对象")
@ExcelIgnoreUnannotated
public class GiftInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 礼包id
     */
    @ExcelProperty(value = "礼包id")
    @ApiModelProperty("礼包id")
    private Integer id;

    /**
     * 礼包名称
     */
    @ExcelProperty(value = "礼包名称")
    @ApiModelProperty("礼包名称")
    private String giftName;

    /**
     * 礼包组id
     */
    @ExcelProperty(value = "礼包组id")
    @ApiModelProperty("礼包组id")
    private Integer giftGroupId;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private String gameId;

    /**
     * 礼包领取等级
     */
    @ExcelProperty(value = "礼包领取等级")
    @ApiModelProperty("礼包领取等级")
    private Integer receiveGrade;

    /**
     * 礼包介绍
     */
    @ExcelProperty(value = "礼包介绍")
    @ApiModelProperty("礼包介绍")
    private String giftIntroduce;

    /**
     * 礼包类型 1 平台币 2余额  3积分
     */
    @ExcelProperty(value = "礼包类型 1 平台币 2余额  3积分")
    @ApiModelProperty("礼包类型 1 平台币 2余额  3积分")
    private String type;

    /**
     * 奖励数量
     */
    @ExcelProperty(value = "奖励数量")
    @ApiModelProperty("奖励数量")
    private Integer rewardAmount;

    /**
     * 礼包图标
     */
    @ExcelProperty(value = "礼包图标")
    @ApiModelProperty("礼包图标")
    private String giftIcon;

    /**
     * 领取状态 0:未达成 1:待领取 2:已领取
     */
    @ExcelProperty(value = "领取状态 0:未达成 1:待领取 2:已领取")
    @ApiModelProperty(value = "领取状态 0:未达成 1:待领取 2:已领取")
    private String receiveStatus;

}
