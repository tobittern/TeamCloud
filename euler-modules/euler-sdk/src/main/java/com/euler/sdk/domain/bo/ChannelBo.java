package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 主渠道业务对象 channel
 *
 * @author euler
 * @date 2022-04-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("主渠道业务对象")
public class ChannelBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Integer id;


    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 渠道名
     */
    @ApiModelProperty(value = "渠道名", required = true)
    @NotBlank(message = "渠道名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String channelName;

    /**
     * 渠道主账号名称
     */
    @ApiModelProperty(value = "渠道主账号名称", required = true)
    @NotBlank(message = "渠道主账号名称不能为空", groups = { AddGroup.class})
    private String adminName;

    /**
     * 设置渠道内游戏
     */
    @ApiModelProperty(value = "设置渠道内游戏", required = true)
    @NotBlank(message = "渠道内游戏不能为空", groups = { AddGroup.class, EditGroup.class})
    private String channelGame;

}
