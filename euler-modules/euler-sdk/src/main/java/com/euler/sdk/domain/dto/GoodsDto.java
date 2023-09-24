package com.euler.sdk.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品业务对象 goods
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("商品业务对象")
public class GoodsDto extends PageQuery {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", required = true)
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
    private Integer goodsType = 1;

    /**
     * 商品等级 1初级 2中级 3高级
     */
    @ApiModelProperty("商品等级 1初级 2中级 3高级")
    private String goodsLevel;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", required = true)
    private String goodsName;


    /**
     * 是否限时 0不限时  1限时
     */
    @ApiModelProperty(value = "是否限时 0不限时  1限时", required = true)
    private Integer isLimitTime;


    /**
     * 是否上线   1上线  2下线
     */
    @ApiModelProperty(value = "是否上线   1上线  2下线", required = true)
    private Integer isUp;

    /**
     * 获取商品的类型 (1平台币 2余额)
     */
    @ApiModelProperty(value = "获取商品的类型 (1平台币 2余额)", required = true)
    private Integer getType;


}
