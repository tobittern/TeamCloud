package com.euler.platform.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;


/**
 * 游戏版本历史业务对象 open_game_version_history
 *
 * @author euler
 * @date 2022-03-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("游戏版本历史业务对象")
public class OpenGameVersionHistoryBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id", groups = {EditGroup.class})
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "游戏id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer gameId;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号", required = true)
    @NotNull(message = "版本号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer versionNumber;

    /**
     * 版本号名称
     */
    @ApiModelProperty(value = "版本号名称", required = true)
    @NotBlank(message = "版本号名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String versionNumberName;

    /**
     * 版本说明
     */
    @ApiModelProperty(value = "版本说明", required = true)
    @NotBlank(message = "版本说明不能为空", groups = {AddGroup.class, EditGroup.class})
    private String versionDescription;

    /**
     * 游戏的图片列表
     */
    @ApiModelProperty(value = "游戏的图片列表", required = true)
    @NotBlank(message = "游戏的图片列表不能为空", groups = {AddGroup.class, EditGroup.class})
    private String pictureUrl;

    /**
     * 游戏安装包地址
     */
    @ApiModelProperty(value = "游戏安装包地址", required = true)
    @NotBlank(message = "游戏安装包地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String gameInstallPackage;


}
