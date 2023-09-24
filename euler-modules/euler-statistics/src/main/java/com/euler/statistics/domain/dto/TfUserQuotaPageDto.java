package com.euler.statistics.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import com.euler.common.mybatis.core.page.PageQuery;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户相关的标准指标统计分页业务对象 tf_user_quota
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户相关的标准指标统计分页业务对象")
public class TfUserQuotaPageDto extends PageQuery {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @ApiModelProperty(value = "", required = true)
    private Integer id;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期", required = true)
    private String dateId;

    /**
     * 渠道id
     */
    @ApiModelProperty(value = "渠道id", required = true)
    private Integer channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty(value = "渠道名称", required = true)
    private String channelName;

    /**
     * 指标标识
     */
    @ApiModelProperty(value = "指标标识", required = true)
    private String measureId;

    /**
     * 指标值
     */
    @ApiModelProperty(value = "指标值", required = true)
    private BigDecimal measureValue;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;


}
