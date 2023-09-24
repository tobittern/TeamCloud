package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 订单视图对象 business_order
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("业务订单视图对象")
@ExcelIgnoreUnannotated
public class BusinessOrderSimpleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    @ExcelProperty(value = "支付订单号")
    @ApiModelProperty("支付订单号")
    private String id;


    /**
     * 订单总金额
     */
    @ExcelProperty(value = "订单总金额")
    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;

    /**
     * 会员id
     */
    @ExcelProperty(value = "会员id")
    @ApiModelProperty("会员id")
    private Long memberId;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏角色id
     */
    @ExcelProperty(value = "游戏角色id")
    @ApiModelProperty("游戏角色id")
    private String gameRoleId;

    /**
     * 区服id
     */
    @ExcelProperty(value = "区服id")
    @ApiModelProperty("区服id")
    private String gameServerId;

    /**
     * 渠道code
     */
    @ExcelProperty(value = "渠道code")
    @ApiModelProperty("渠道code")
    private String gamePackageCode;


    /**
     * 格式化的时间
     */
    @ExcelProperty(value = "格式化的时间")
    @ApiModelProperty("格式化的时间")
    private String dateFormat;



    /**
     * 格式化的时间 数字格式
     */
    @ExcelProperty(value = "格式化的时间 数字格式")
    @ApiModelProperty("格式化的时间 数字格式")
    private Integer dateFormatNumbers;



    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


}
