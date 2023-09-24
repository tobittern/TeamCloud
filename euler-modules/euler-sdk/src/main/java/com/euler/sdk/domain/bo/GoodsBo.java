package com.euler.sdk.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品业务对象 goods
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("商品业务对象")
public class GoodsBo extends BaseEntity {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(message = "主键ID不能为空", groups = {EditGroup.class})
    private Integer id;

    /**
     * 添加用户ID
     */
    @ApiModelProperty(value = "添加用户ID", required = true)
    private Long userId;

    /**
     * 商品类型 （1年费商品 2充值商品）
     */
    @ApiModelProperty(value = "商品类型 （1年费商品 2充值商品）", required = true)
    private String goodsType = "1";

    /**
     * 商品icon
     */
    @ApiModelProperty(value = "商品icon", required = true)
    @NotBlank(message = "商品icon不能为空", groups = {AddGroup.class, EditGroup.class})
    private String goodsIcon;

    /**
     * 商品的背景图
     */
    @ApiModelProperty(value = "商品icon", required = true)
    private String goodsBackgroud;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String goodsName;

    /**
     * 商品等级 1初级 2中级 3高级
     */
    @ApiModelProperty("商品等级 1初级 2中级 3高级")
    private Integer goodsLevel = 1;

    /**
     * 商品折扣
     */
    @ApiModelProperty("商品折扣")
    private String goodsDiscount;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格", required = true)
    @NotNull(message = "商品价格不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal goodsPrice;

    /**
     * 商品划线价格
     */
    @ApiModelProperty(value = "商品划线价格", required = true)
    @NotNull(message = "商品划线价格不能为空", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal goodsScribePrice;

    /**
     * 前台展示的时长
     */
    @ApiModelProperty(value = "前台展示的时长", required = true)
    private String showDuration = "1年";

    /**
     * 时长 - 按天计算
     */
    @ApiModelProperty(value = "时长 - 按天计算", required = true)
    private Integer duration = 1;

    /**
     * 获取的平台币 - 立即获取多少平台币
     */
    @ApiModelProperty(value = "获取的平台币 - 立即获取多少平台币", required = true)
    private Integer platformCurrency = 0;

    /**
     * 每月获取的平台币
     */
    @ApiModelProperty(value = "每月获取的平台币", required = true)
    private Integer everyMonthPlatformCurrency = 0;

    /**
     * 赠送比例
     */
    @ApiModelProperty(value = "赠送比例", required = true)
    private Double proportion = 0.00;

    /**
     * 获取商品的类型 (1平台币 2余额)
     */
    @ApiModelProperty(value = "获取商品的类型 (1平台币 2余额)", required = true)
    private Integer getType = 1;

    /**
     * 是否限时 0不限时  1限时
     */
    @ApiModelProperty(value = "是否限时 0不限时  1限时", required = true)
    private Integer isLimitTime = 0;

    /**
     * 限时开始时间
     */
    @ApiModelProperty(value = "限时开始时间", required = true)
    private Date limitTimeStart;

    /**
     * 限时结束时间
     */
    @ApiModelProperty(value = "限时结束时间", required = true)
    private Date limitTimeEnd;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述", required = true)
    private String goodsDesc;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort = 0;

    /**
     * 是否上线  0不上线  1上线
     */
    @ApiModelProperty(value = "是否上线   1上线  2下线", required = true)
    private Integer isUp = 1;


}
