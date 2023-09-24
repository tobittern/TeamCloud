package com.euler.sdk.domain.bo;

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
 * 业务对象 statistics_charge
 *
 * @author euler
 *  2022-07-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class StatisticsChargeBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道id", required = true)
    @NotNull(message = "渠道id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称", required = true)
    @NotBlank(message = "渠道名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String channelName;

    /**
     * 总人数
     */
    @ApiModelProperty(value = "总人数", required = true)
    @NotNull(message = "总人数不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal userTotal;

    /**
     * 总充值
     */
    @ApiModelProperty(value = "总充值", required = true)
    @NotNull(message = "总充值不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal chargeTotal;

    /**
     * 总订单
     */
    @ApiModelProperty(value = "总订单", required = true)
    @NotNull(message = "总订单不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal orderTotal;

    /**
     * 统计日期，所在渠道人数
     */
    @ApiModelProperty(value = "统计日期，所在渠道人数", required = true)
    @NotNull(message = "统计日期，所在渠道人数不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal userNum;

    /**
     * 统计日期，所在渠道充值金额
     */
    @ApiModelProperty(value = "统计日期，所在渠道充值金额", required = true)
    @NotNull(message = "统计日期，所在渠道充值金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal chargeNum;

    /**
     * 统计日期，所在渠道订单数目
     */
    @ApiModelProperty(value = "统计日期，所在渠道订单数目", required = true)
    @NotNull(message = "统计日期，所在渠道订单数目不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal orderNum;

    /**
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期", required = true)
    @NotNull(message = "统计日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date day;


}
