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


/**
 * 渠道分组业务对象 channel_group
 *
 * @author euler
 * @date 2022-04-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("渠道分组业务对象")
public class ChannelPackageBo extends BaseEntity {

    /**
     * 主渠道ID
     */
    @ApiModelProperty(value = "主渠道ID", required = true)
    @NotNull(message = "主渠道ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer channelId;

    /**
     * 对应游戏ID
     */
    @ApiModelProperty(value = "对应游戏ID", required = true)
    @NotNull(message = "对应游戏ID", groups = {AddGroup.class, EditGroup.class})
    private Integer gameId;

    /**
     * 分包任务id
     */
    @ApiModelProperty(value = "分包任务id", required = true)
    private Integer packageTaskId;

    /**
     * 新游戏名
     */
    @ApiModelProperty(value = "新游戏名", required = true)
    @NotBlank(message = "新游戏名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newGameName;

    /**
     * 分包的icon
     */
    @ApiModelProperty(value = "分包的icon", required = true)
    @NotBlank(message = "分包的icon不能为空", groups = {AddGroup.class, EditGroup.class})
    private String icon;

    /**
     * 分包的前缀名称
     */
    @ApiModelProperty(value = "分包的前缀名称", required = true)
    @NotBlank(message = "分包的前缀名称不能为空", groups = {AddGroup.class})
    private String packagePrefixName;

    /**
     * 分包的名称
     */
    @ApiModelProperty(value = "分包的名称", required = true)
    private String packageCode;

    /**
     * 分包的标签
     */
    @ApiModelProperty(value = "分包的标签", required = true)
    @NotBlank(message = "分包的标签不能为空", groups = {AddGroup.class, EditGroup.class})
    private String label;

    /**
     * 版本id
     */
    @ApiModelProperty(value = "版本id", required = true)
    @NotNull(message = "版本id不能为空", groups = {AddGroup.class})
    private Integer versionId;

    /**
     * 版本
     */
    @ApiModelProperty(value = "版本", required = true)
    @NotBlank(message = "版本不能为空", groups = {AddGroup.class})
    private String version;

    /**
     * 分包数量
     */
    @ApiModelProperty(value = "分包数量", required = true)
    private Integer numberOfSubcontracts = 1;

    /**
     * 分保渠道版本 其实值得就是次数
     */
    @ApiModelProperty(value = "分保渠道版本 其实值得就是次数", required = true)
    private Integer edition;

}
