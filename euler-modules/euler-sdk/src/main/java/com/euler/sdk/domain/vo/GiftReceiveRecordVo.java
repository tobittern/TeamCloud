package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 礼包领取记录视图对象 gift_receive_record
 *
 * @author euler
 * @date 2022-04-13
 */
@Data
@ApiModel("礼包领取记录视图对象")
@ExcelIgnoreUnannotated
public class GiftReceiveRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 礼包类型 1:等级礼包 2:活动礼包
     */
    @ExcelProperty(value = "礼包类型 1:等级礼包 2:活动礼包")
    @ApiModelProperty("礼包类型 1:等级礼包 2:活动礼包")
    private String giftType;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Integer memberId;

    /**
     * 领取状态 0:未达成 1:待领取 2:已领取
     */
    @ExcelProperty(value = "领取状态 0:未达成 1:待领取 2:已领取")
    @ApiModelProperty("领取状态 0:未达成 1:待领取 2:已领取")
    private String receiveStatus;

    /**
     * 礼包id
     */
    @ExcelProperty(value = "礼包id")
    @ApiModelProperty("礼包id")
    private Integer giftId;

}
