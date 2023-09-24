package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户黑名单列业务对象 member_blacklist
 *
 * @author euler
 * @date 2022-06-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户黑名单列业务对象")
public class MemberBlacklistBo extends BaseEntity {

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键", required = true)
    @NotNull(message = "自增主键不能为空", groups = {EditGroup.class})
    private Integer id;

    /**
     * 平台 0全平台 1sdk  2app
     */
    @ApiModelProperty(value = "平台 0全平台 1sdk  2app", required = true)
    private Integer platform = 0;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberId;

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private String gameId;

    /**
     * 封号截止时间
     */
    @ApiModelProperty(value = "封号截止时间", required = true)
    private Date endTime;

    /**
     * 封号原因
     */
    @ApiModelProperty(value = "封号原因", required = true)
    private String record;

    /**
     * 是否是永久封号  0 不是  1是
     */
    @ApiModelProperty(value = "是否是永久封号  0 不是  1是", required = true)
    private Integer isPermanent;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * 类型 1用户 2IP
     */
    @ApiModelProperty("类型 1用户 2IP")
    private Integer type;


}
