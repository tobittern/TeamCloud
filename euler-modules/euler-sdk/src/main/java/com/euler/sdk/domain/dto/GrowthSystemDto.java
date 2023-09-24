package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 成长体系业务对象 growth_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("成长体系业务对象")
public class GrowthSystemDto extends PageQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 游戏用户id
     */
    @ApiModelProperty(value = "游戏用户id")
    private Long userId;

    /**
     * 等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊
     */
    @ApiModelProperty(value = "等级类型 1:黑铁 2:青铜 3:白银 4:黄金 5:钻石 6:黑钻 7:至尊")
    private Integer gradeType;

    /**
     * 成长等级(1~10)
     */
    @ApiModelProperty(value = "成长等级(1~10)")
    private Integer growthGrade;

    /**
     * 用户充值的钱
     */
    @ApiModelProperty(value = "用户充值的钱")
    private Long money;

}
