package com.euler.sdk.api.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 业务对象 sign_in
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class SignInDto extends PageQuery {

    /**
     * 签到用户
     */
    @ApiModelProperty(value = "签到用户", required = true)
    private Integer memberId;

    /**
     * 签到活动ID
     */
    @ApiModelProperty(value = "签到活动ID", required = true)
    private Integer activeId;

    /**
     * 签到日期
     */
    @ApiModelProperty(value = "签到日期", required = true)
    private Integer week;

}
