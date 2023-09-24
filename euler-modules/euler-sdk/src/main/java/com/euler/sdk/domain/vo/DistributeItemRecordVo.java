package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 派发物品记录视图对象 distribute_item_record
 *
 * @author euler
 * @date 2022-04-09
 */
@Data
@ApiModel("派发物品记录视图对象")
@ExcelIgnoreUnannotated
public class DistributeItemRecordVo {

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
     * 派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡
     */
    @ExcelProperty(value = "派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡")
    @ApiModelProperty("派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡")
    private String distributeType;

    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    @ApiModelProperty(value = "商品id", required = true)
    private Integer goodsId;

    /**
     * 派发数量
     */
    @ExcelProperty(value = "派发数量")
    @ApiModelProperty("派发数量")
    private BigDecimal distributeAmount;

    /**
     * 派发说明
     */
    @ExcelProperty(value = "派发说明")
    @ApiModelProperty("派发说明")
    private String remark;

}
