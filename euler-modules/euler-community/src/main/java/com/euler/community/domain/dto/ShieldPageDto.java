package com.euler.community.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;


/**
 * 屏蔽信息分页业务对象 shield
 *
 * @author euler
 * @date 2022-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("屏蔽信息分页业务对象")
public class ShieldPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;


    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    /**
     * 业务类型，1：用户：2动态
     */
    @ApiModelProperty(value = "业务类型，1：用户：2动态")
    private Integer businessType;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    private String reason;

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
