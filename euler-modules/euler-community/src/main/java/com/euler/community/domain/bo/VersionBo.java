package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;
/**
 * 版本管理业务对象 version
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("版本管理业务对象")
public class VersionBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", required = true)
    @Size(min = 1, max = 30, message = "标题必须限制为15个字以内")
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 应用系统，'1': android  '2': ios
     */
    @ApiModelProperty(value = "应用系统，'1': android  '2': ios", required = true)
    @NotBlank(message = "应用系统，'1': android  '2': ios不能为空", groups = { AddGroup.class, EditGroup.class })
    private String systemType;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号", required = true)
    @NotBlank(message = "版本号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String versionNo;

    /**
     * ios的时候，下载地址
     */
    @ApiModelProperty(value = "ios的时候，下载地址")
    private String downloadUrl;

    /**
     * 附件名称
     */
    @ApiModelProperty(value = "附件名称")
    private String fileName;

    /**
     * 附件路径
     */
    @ApiModelProperty(value = "附件路径")
    private String filePath;

    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    /**
     * 更新方式，'1':推荐更新 '2':强制更新
     */
    @ApiModelProperty(value = "更新方式，'1':推荐更新 '2':强制更新", required = true)
    @NotBlank(message = "更新方式，'1':推荐更新 '2':强制更新不能为空", groups = { AddGroup.class, EditGroup.class })
    private String updateType;

    /**
     * 版本状态，'1':待发布 '2':已发布 '3':已下架
     */
    @ApiModelProperty(value = "版本状态，'1':待发布 '2':已发布 '3':已下架")
    private String versionStatus;

    @ApiModelProperty("发现页开关：1：开，0：关")
    private String discoverSwitch="0";

}
