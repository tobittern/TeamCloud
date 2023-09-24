package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 成长体系视图对象 growth_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("成长体系视图对象")
@ExcelIgnoreUnannotated
public class GrowthSystemVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 游戏用户id
     */
    @ExcelProperty(value = "游戏用户id")
    @ApiModelProperty("游戏用户id")
    private Long userId;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    @ExcelProperty(value = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊")
    @ApiModelProperty("等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊")
    private Integer gradeType;

    /**
     * 成长等级(1~10)
     */
    @ExcelProperty(value = "成长等级(1~10)")
    @ApiModelProperty("成长等级(1~10)")
    private Integer growthGrade;

    /**
     * 一元能够兑换的成长值
     */
    @ExcelProperty(value = "一元能够兑换的成长值")
    @ApiModelProperty("一元能够兑换的成长值")
    private Integer growthValue;

    /**
     * 用户充值的钱
     */
    @ExcelProperty(value = "用户充值的钱")
    @ApiModelProperty("用户充值的钱")
    private Long money;

    /**
     * 充值所兑换的成长值
     */
    @ExcelProperty(value = "充值所兑换的成长值")
    @ApiModelProperty("充值所兑换的成长值")
    private Long moneyGrowthValue;

    /**
     * 总成长值
     */
    @ExcelProperty(value = "总成长值")
    @ApiModelProperty("总成长值")
    private Long growthTotal;

}
