package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员权益视图对象 member_rights
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("会员权益视图对象")
@ExcelIgnoreUnannotated
public class MemberRightsVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 会员名称
     */
    @ExcelProperty(value = "会员名称")
    @ApiModelProperty("会员名称")
    private String name;

    /**
     * 会员等级 1:初级 2:中级 3:高级
     */
    @ExcelProperty(value = "会员等级 1:初级 2:中级 3:高级")
    @ApiModelProperty("会员等级 1:初级 2:中级 3:高级")
    private String lever;

    /**
     * 会员状态：1:生效中 2:已失效
     */
    @ExcelProperty(value = "会员状态：1:生效中 2:已失效")
    @ApiModelProperty("会员状态：1:生效中 2:已失效")
    private String status;

    /**
     * 是否升级：0:未升级 1:升级
     */
    @ExcelProperty(value = "是否升级：0:未升级 1:升级")
    @ApiModelProperty("是否升级：0:未升级 1:升级")
    private String isUpgrade;


    /**
     * 会员时长
     */
    @ExcelProperty(value = "会员时长")
    @ApiModelProperty("会员时长")
    private Integer memberDuration;

    /**
     * 有效期开始时间
     */
    @ExcelProperty(value = "有效期开始时间")
    @ApiModelProperty("有效期开始时间")
    private Date validateStartTime;

    /**
     * 有效期结束时间
     */
    @ExcelProperty(value = "有效期结束时间")
    @ApiModelProperty("有效期结束时间")
    private Date validateEndTime;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private  Integer goodsId;

    /**
     * 会员权益状态
     */
    @ApiModelProperty("会员权益是否领取 0未领取 1已领取")
    private Integer rightsIsReceive = 0;

    /**
     * 领取类型
     */
    @ApiModelProperty("领取类型")
    private Integer type;

    /**
     * 每月获取的平台币
     */
    @ApiModelProperty("每月获取的平台币")
    private Integer everyMonthPlatformCurrency = 0;

}
