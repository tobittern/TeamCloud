package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
/**
 * 礼包配置业务对象 gift_bag
 *
 * @author euler
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包配置业务对象")
public class GiftBagBo extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", required = true)
    @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotNull(message = "游戏id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称", required = true)
    @NotBlank(message = "游戏名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameName;

    /**
     * 礼包名称
     */
    @ApiModelProperty(value = "礼包名称", required = true)
    @NotBlank(message = "礼包名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String giftName;

    /**
     * 激活码
     */
    @ApiModelProperty(value = "激活码")
    private String activationCode;

    /**
     * 礼包图片名称
     */
    @ApiModelProperty(value = "礼包图片名称")
    private String picName;

    /**
     * 礼包图片路径
     */
    @ApiModelProperty(value = "礼包图片路径", required = true)
    @NotBlank(message = "礼包图片路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String picPath;

    /**
     * 礼包内容
     */
    @ApiModelProperty(value = "礼包内容", required = true)
    @NotBlank(message = "礼包内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 使用方式
     */
    @ApiModelProperty(value = "使用方式", required = true)
    @NotBlank(message = "使用方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String useMethod;

    /**
     * 有效期-开始时间
     */
    @ApiModelProperty(value = "有效期-开始时间", required = true)
    @NotNull(message = "有效期-开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 有效期-结束时间
     */
    @ApiModelProperty(value = "有效期-结束时间", required = true)
    @NotNull(message = "有效期-结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 礼包总数
     */
    @ApiModelProperty(value = "礼包总数")
    private Integer totalNum;

    /**
     * 礼包领取数目
     */
    @ApiModelProperty(value = "礼包领取数目")
    private Integer drawNum;

    /**
     * 礼包兑换数目
     */
    @ApiModelProperty(value = "礼包兑换数目")
    private Integer exchangeNum;

    /**
     * 状态，状态，0待上架，1已上架，2已下架
     */
    @ApiModelProperty(value = "状态，0待上架，1已上架，2已下架")
    private Integer status;

    /**
     * 资源表主键id,用于附件下载
     */
    @ApiModelProperty(value = "资源表主键id,用于附件下载")
    private Long ossId;


    /**
     * cdk礼包文件名称
     */
    @ApiModelProperty(value = "cdk礼包文件名称")
    private String cdkFileName;

    /**
     * cdk礼包文件路径
     */
    @ApiModelProperty(value = "cdk礼包文件路径", required = true)
    @NotBlank(message = "cdk礼包文件路径不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cdkFilePath;

    /**
     * 应用平台 1：android 2：ios 3：h5
     */
    @ApiModelProperty(value = "应用平台", required = true)
    @NotBlank(message = "应用平台不能为空", groups = { AddGroup.class, EditGroup.class })
    private String applicationType;

    /**
     * ios下载地址
     */
    @ApiModelProperty("ios下载地址")
    private String iosDownloadUrl;

    /**
     * android下载地址
     */
    @ApiModelProperty("android下载地址")
    private String androidDownloadUrl;

    /**
     * h5下载地址
     */
    @ApiModelProperty("h5下载地址")
    private String h5DownloadUrl;
}
