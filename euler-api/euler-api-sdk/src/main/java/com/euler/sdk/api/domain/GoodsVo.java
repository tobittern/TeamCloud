package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 视图对象 goods
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("商品视图对象")
@ExcelIgnoreUnannotated
public class GoodsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    @ApiModelProperty("主键ID")
    private Integer id;

    /**
     * 添加用户ID
     */
    @ExcelProperty(value = "添加用户ID")
    @ApiModelProperty("添加用户ID")
    private Long userId;

    /**
     * 商品类型 （1实物 2现金）
     */
    @ExcelProperty(value = "商品类型 ", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=年费商品,2=充值商品")
    @ApiModelProperty("商品类型 （1年费商品 2充值商品）")
    private Integer goodsType;

    /**
     * 商品icon
     */
    @ExcelProperty(value = "商品icon")
    @ApiModelProperty("商品icon")
    private String goodsIcon;

    /**
     * 商品的背景图
     */
    @ExcelProperty(value = "商品的背景图")
    @ApiModelProperty("商品的背景图")
    private String goodsBackgroud;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品等级 1初级 2中级 3高级
     */
    @ExcelProperty(value = "商品等级 1初级 2中级 3高级")
    @ApiModelProperty("商品等级 1初级 2中级 3高级")
    private String goodsLevel;

    /**
     * 商品折扣
     */
    @ExcelProperty(value = "商品折扣")
    @ApiModelProperty("商品折扣")
    private String goodsDiscount;

    /**
     * 商品价格
     */
    @ExcelProperty(value = "商品价格")
    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

    /**
     * 商品优惠价格
     */
    @ExcelProperty(value = "商品优惠价格")
    @ApiModelProperty("商品优惠价格")
    private BigDecimal goodsDiscountPrice;

    /**
     * 商品划线价格
     */
    @ExcelProperty(value = "商品划线价格")
    @ApiModelProperty("商品划线价格")
    private BigDecimal goodsScribePrice;

    /**
     * 前台展示的时长
     */
    @ExcelProperty(value = "前台展示的时长")
    @ApiModelProperty("前台展示的时长")
    private String showDuration;

    /**
     * 时长 - 按天计算
     */
    @ExcelProperty(value = "时长 - 按天计算")
    @ApiModelProperty("时长 - 按天计算")
    private Integer duration;

    /**
     * 获取的平台币 - 立即获取多少平台币
     */
    @ExcelProperty(value = "获取的平台币")
    @ApiModelProperty("获取的平台币 - 立即获取多少平台币")
    private Integer platformCurrency;

    /**
     * 每月获取的平台币
     */
    @ExcelProperty(value = "每月获取的平台币")
    @ApiModelProperty("每月获取的平台币")
    private Integer everyMonthPlatformCurrency;

    /**
     * 赠送比例
     */
    @ExcelProperty(value = "赠送比例")
    @ApiModelProperty("赠送比例")
    private Double proportion;

    /**
     * 获取商品的类型 (1平台币 2余额)
     */
    @ExcelProperty(value = "获取商品的类型")
    @ApiModelProperty("获取商品的类型 (1平台币 2余额)")
    private Integer getType;

    /**
     * 是否限时 0不限时  1限时
     */
    @ExcelProperty(value = "是否限时")
    @ApiModelProperty("是否限时 0不限时  1限时")
    private Integer isLimitTime;

    /**
     * 限时开始时间
     */
    @ExcelProperty(value = "限时开始时间")
    @ApiModelProperty("限时开始时间")
    private Date limitTimeStart;

    /**
     * 限时结束时间
     */
    @ExcelProperty(value = "限时结束时间")
    @ApiModelProperty("限时结束时间")
    private Date limitTimeEnd;

    /**
     * 商品描述
     */
    @ExcelProperty(value = "商品描述")
    @ApiModelProperty("商品描述")
    private String goodsDesc;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 是否上线  0不上线  1上线
     */
    @ExcelProperty(value = "是否上线")
    @ApiModelProperty("是否上线   1上线  2下线")
    private Integer isUp;

    /**
     * 会员权益是否拥有
     */
    @ExcelProperty(value = "会员权益是否拥有")
    @ApiModelProperty("会员权益是否拥有 0不拥有 1拥有")
    private Integer memberRightsHave = 0;

    /**
     * 会员权益是否领取
     */
    @ExcelProperty(value = "会员权益是否领取")
    @ApiModelProperty("会员权益是否领取 0未领取 1已领取")
    private Integer rightsIsReceive = 0;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
