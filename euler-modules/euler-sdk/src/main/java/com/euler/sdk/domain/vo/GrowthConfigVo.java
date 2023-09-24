package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 成长值配置视图对象 growth_config
 *
 * @author euler
 * @date 2022-03-25
 */
@Data
@ApiModel("成长值配置视图对象")
@ExcelIgnoreUnannotated
public class GrowthConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 等级名称
     */
    @ExcelProperty(value = "等级名称")
    @ApiModelProperty("等级名称")
    private String name;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    @ExcelProperty(value = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊")
    @ApiModelProperty("等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊")
    private Integer type;

    /**
     * 成长等级(1~10)
     */
    @ExcelProperty(value = "成长等级(1~10)")
    @ApiModelProperty("成长等级(1~10)")
    private Integer grade;

    /**
     * 梯度
     */
    @ExcelProperty(value = "梯度")
    @ApiModelProperty("梯度")
    private Integer gradient;

    /**
     * 升级所需要的成长值
     */
    @ExcelProperty(value = "升级所需要的成长值")
    @ApiModelProperty("升级所需要的成长值")
    private Long upgradeValue;

}
