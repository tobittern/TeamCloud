package com.euler.sdk.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对象 goods
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods")
public class Goods extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 添加用户ID
     */
    private Long userId;
    /**
     * 商品类型 （1年费商品 2充值商品）
     */
    private Integer goodsType;
    /**
     * 商品icon
     */
    private String goodsIcon;
    /**
     * 商品的背景图
     */
    private String goodsBackgroud;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品等级 1初级 2中级 3高级
     */
    private String goodsLevel;
    /**
     * 商品折扣
     */
    private String goodsDiscount;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 商品划线价格
     */
    private BigDecimal goodsScribePrice;
    /**
     * 前台展示的时长
     */
    private String showDuration;

    /**
     * 时长 - 按天计算
     */
    private Integer duration;
    /**
     * 获取的平台币
     */
    private Integer platformCurrency;
    /**
     * 每月获取的平台币
     */
    private Integer everyMonthPlatformCurrency;
    /**
     * 赠送比例
     */
    private Double proportion;

    /**
     * 获取商品的类型 (1平台币 2余额)
     */
    private Integer getType;
    /**
     * 是否限时 0不限时  1限时
     */
    private Integer isLimitTime;
    /**
     * 限时开始时间
     */
    private Date limitTimeStart;
    /**
     * 限时结束时间
     */
    private Date limitTimeEnd;
    /**
     * 商品描述
     */
    private String goodsDesc;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否上线   1上线  2下线
     */
    private Integer isUp;
    /**
     * 删除状态 0不删除 1删除
     */
     @TableLogic
    private String delFlag;

}
