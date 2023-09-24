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
 * sdk版本管理业务对象 version
 *
 * @author euler
 * @date 2022-07-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("sdk版本管理业务对象")
public class VersionBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号", required = true)
    @NotBlank(message = "版本号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String number;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)", required = true)
    //@NotNull(message = "运行平台 (1 安卓  2 ios  3 h5)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer platform;

    /**
     * 版本类型，0稳定版，1最新版
     */
    @ApiModelProperty(value = "版本类型，0稳定版，1最新版", required = true)
    //@NotNull(message = "版本类型，0稳定版，1最新版不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer type;

    /**
     * 是否是新版本，0是新版本，1历史版本
     */
    @ApiModelProperty(value = "是否是新版本，0是新版本，1历史版本", required = true)
    //@NotNull(message = "是否是新版本，0是新版本，1历史版本不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer isNew;

    /**
     * 更新内容
     */
    @ApiModelProperty(value = "更新内容", required = true)
    @NotBlank(message = "更新内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径", required = true)
    //@NotBlank(message = "文件路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileUrl;


}
