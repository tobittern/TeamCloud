package com.euler.risk.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import com.euler.common.mybatis.core.page.PageQuery;

import java.util.Date;


/**
 * ip汇总分页业务对象 tf_ip_summary
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("ip汇总分页业务对象")
public class TfIpSummaryPageDto extends PageQuery {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 汇总日期
     */
    @ApiModelProperty(value = "汇总日期", required = true)
    private Date dateId;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址", required = true)
    private String ip;

    /**
     * ip区域地址
     */
    @ApiModelProperty(value = "ip区域地址", required = true)
    private String ipAddress;

    /**
     * 注册次数
     */
    @ApiModelProperty(value = "注册次数", required = true)
    private Integer registerNum;

    /**
     * 登录次数
     */
    @ApiModelProperty(value = "登录次数", required = true)
    private Integer loginNum;

    /**
     * 角色创建次数
     */
    @ApiModelProperty(value = "角色创建次数", required = true)
    private Integer roleCreateNum;



}
