package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.domain.dto.KeyValueDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 礼包管理视图对象 gift_management
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("礼包管理视图对象")
@ExcelIgnoreUnannotated
public class GiftManagementVo {

    private static final long serialVersionUID = 1L;

    /**
     * 礼包组id
     */
    @ExcelProperty(value = "礼包组id")
    @ApiModelProperty("礼包组id")
    private Integer id;

    /**
     * 礼包组名
     */
    @ExcelProperty(value = "礼包组名")
    @ApiModelProperty("礼包组名")
    private String giftGroupName;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 面向游戏
     */
    @ExcelProperty(value = "面向游戏")
    @ApiModelProperty("面向游戏")
    private List<KeyValueDto> gameOriented;

    /**
     * 礼包数量
     */
    @ExcelProperty(value = "礼包数量")
    @ApiModelProperty("礼包数量")
    private Integer giftAmount;

    /**
     * 礼包领取等级
     */
    @ExcelProperty(value = "礼包领取等级")
    @ApiModelProperty("礼包领取等级")
    private String receiveGrade;

    /**
     * 是否上架 1:上架 2:下架
     */
    @ExcelProperty(value = "是否上架 1:上架 2:下架")
    @ApiModelProperty("是否上架 1:上架 2:下架")
    private String isUp;

    /**
     * 礼包组图标
     */
    @ExcelProperty(value = "礼包组图标")
    @ApiModelProperty("礼包组图标")
    private String giftGroupIcon;

}
