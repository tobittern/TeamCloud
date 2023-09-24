package com.euler.sdk.domain.vo;

import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 活动视图对象 activity
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("活动视图对象")
@ExcelIgnoreUnannotated
public class ActivityVo {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @ExcelProperty(value = "活动id")
    @ApiModelProperty("活动id")
    private Integer id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 活动名称
     */
    @ExcelProperty(value = "活动名称")
    @ApiModelProperty("活动名称")
    private String name;

//    /**
//     * 面向游戏的id
//     */
//    @ExcelProperty(value = "面向游戏的id")
//    @ApiModelProperty("面向游戏的id")
//    private List<Integer> gameOrientedId;

    /**
     * 面向游戏的名称
     */
    @ExcelProperty(value = "面向游戏的名称")
    @ApiModelProperty("面向游戏的名称")
    private List<KeyValueDto> gameOriented;

    /**
     * 活动类型( 1：限时折扣模块  2：热门活动模块)
     */
    @ExcelProperty(value = "活动类型( 1：限时折扣模块  2：热门活动模块)")
    @ApiModelProperty("活动类型( 1：限时折扣模块  2：热门活动模块)")
    private String activityType;

    /**
     * 活动banner
     */
    @ExcelProperty(value = "活动banner")
    @ApiModelProperty("活动banner")
    private String activeBanner;

    /**
     * 跳转地址
     */
    @ExcelProperty(value = "跳转地址")
    @ApiModelProperty("跳转地址")
    private String jumpAddress;

    /**
     * 活动开启时间
     */
    @ExcelProperty(value = "活动开启时间")
    @ApiModelProperty("活动开启时间")
    private Date activityStartTime;

    /**
     * 活动关闭时间
     */
    @ExcelProperty(value = "活动关闭时间")
    @ApiModelProperty("活动关闭时间")
    private Date activityClosingTime;

    /**
     * 是否上线 0上线  1下线
     */
    @ExcelProperty(value = "是否上线 0上线  1下线")
    @ApiModelProperty("是否上线 0上线  1下线")
    private Integer isOnline;


}
