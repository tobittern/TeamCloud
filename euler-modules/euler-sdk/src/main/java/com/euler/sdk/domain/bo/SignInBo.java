package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 业务对象 sign_in
 *
 * @author euler
 * @date 2022-03-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class SignInBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 签到用户
     */
    @ApiModelProperty(value = "签到用户", required = true)
    @NotNull(message = "签到用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /**
     * 签到活动ID
     */
    @ApiModelProperty(value = "签到活动ID", required = true)
    @NotNull(message = "签到活动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer activeId;

    /**
     * 签到日期
     */
    @ApiModelProperty(value = "签到日期", required = true)
    @NotNull(message = "签到日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer week;

    /**
     * 积分数
     */
    @ApiModelProperty(value = "积分数", required = true)
    @NotNull(message = "积分数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer score;


}
