package com.euler.sdk.domain.bo;

import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 黑名单封禁操作记录业务对象 member_blacklist_record
 *
 * @author euler
 * @date 2022-06-16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("黑名单封禁操作记录业务对象")
public class MemberBlacklistRecordBo extends BaseEntity {

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long memberId;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    private String description;

    /**
     * 封号截止时间
     */
    @ApiModelProperty(value = "封号截止时间", required = true)
    private String endTime;

    /**
     * 封号原因
     */
    @ApiModelProperty(value = "封号原因", required = true)
    private String record;

    /**
     * 操作类型 1封号 2解封
     */
    @ApiModelProperty(value = "操作类型 1封号 2解封", required = true)
    private Integer type;

}
