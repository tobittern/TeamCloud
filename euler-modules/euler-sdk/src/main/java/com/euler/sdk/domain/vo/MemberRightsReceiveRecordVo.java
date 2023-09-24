package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员权益领取记录视图对象 member_rights_receive_record
 *
 * @author euler
 * @date 2022-04-08
 */
@Data
@ApiModel("会员权益领取记录视图对象")
@ExcelIgnoreUnannotated
public class MemberRightsReceiveRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 会员id
     */
    @ExcelProperty(value = "会员id")
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分
     */
    @ExcelProperty(value = "领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分")
    @ApiModelProperty("领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分")
    private String type;

    /**
     * 当月领取平台币
     */
    @ExcelProperty(value = "当月领取平台币")
    @ApiModelProperty("当月领取平台币")
    private Integer receiveMonthPlatformCurrency;

    /**
     * 立即领取的平台币
     */
    @ExcelProperty(value = "立即领取的平台币")
    @ApiModelProperty("立即领取的平台币")
    private Integer receivePlatformCurrency;

    /**
     * 领取的积分
     */
    @ExcelProperty(value = "领取的积分")
    @ApiModelProperty("领取的积分")
    private Integer receiveScore;

    /**
     * 当前年份
     */
    @ExcelProperty(value = "当前年份")
    @ApiModelProperty("当前年份")
    private Integer currentYear;

    /**
     * 当前月份
     */
    @ExcelProperty(value = "当前月份")
    @ApiModelProperty("当前月份")
    private Integer currentMonth;

}
