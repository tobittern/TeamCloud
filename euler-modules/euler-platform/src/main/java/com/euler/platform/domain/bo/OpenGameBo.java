package com.euler.platform.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 【游戏管理】业务对象 open_game
 *
 * @author open
 * @date 2022-02-18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("【游戏管理】业务对象")
public class OpenGameBo extends BaseEntity {

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键自增")
    @NotNull(message = "主键自增不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;
//
//    /**
//     * 游戏id
//     */
//    @ApiModelProperty(value = "游戏id")
//    @NotNull(message = "游戏id不能为空", groups = { AddGroup.class})
//    private Long gameId;

    /**
     * 游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)
     */
    @ApiModelProperty(value = "游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)")
    private Integer gameStatus;

    /**
     * 游戏上架状态(0待上线  1上线  2下线)
     */
    @ApiModelProperty(value = "游戏上架状态(0待上线  1上线  2下线)")
    private Integer onlineStatus;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称", required = true)
    @NotBlank(message = "游戏名称不能为空", groups = { AddGroup.class})
    @Size(min = 1, max = 15, message = "游戏名称长度不能超过15个字符")
    private String gameName;

    /**
     * 游戏类目
     */
    @ApiModelProperty(value = "游戏类目", required = true)
    @NotNull(message = "游戏类目不能为空", groups = { AddGroup.class})
    private String gameCategory;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)", required = true)
    @NotNull(message = "运行平台不能为空", groups = { AddGroup.class})
    private Integer operationPlatform;

    /**
     * 充值回调
     */
    @ApiModelProperty(value = "充值回调")
    private String rechargeCallback;

    /**
     * 回调秘钥
     */
    @ApiModelProperty(value = "回调秘钥")
    private String callbackSecretKey;

    /**
     * icon图片地址
     */
    @ApiModelProperty(value = "icon图片地址", required = true)
    @NotBlank(message = "icon图片地址不能为空", groups = { AddGroup.class })
    private String iconUrl;

    /**
     * 游戏的图片列表
     */
    @ApiModelProperty(value = "游戏的图片列表")
    @NotBlank(message = "游戏的图片列表不能为空")
    private String pictureUrl;

    /**
     * 游戏简介
     */
    @Xss(message = "游戏简介不能包含脚本字符")
    @ApiModelProperty(value = "游戏简介")
    @Size(min = 1, max = 120, message = "游戏简介长度不能超过120个字符")
    private String gameIntroduction;

    /**
     * 付费类型(1 有付费 2无付费)
     */
    @ApiModelProperty(value = "付费类型(1 有付费 2无付费)")
    @NotNull(message = "付费类型不能为空", groups = { AddGroup.class})
    private Integer payType;

    /**
     * 游戏标签 多个用逗号隔开
     */
    @ApiModelProperty(value = "游戏标签 多个用逗号隔开")
    private String gameTags;

    /**
     * 游戏的安装包
     */
    @ApiModelProperty(value = "游戏的安装包")
    private String gameInstallPackage;

    /**
     * 游戏包名
     */
    @ApiModelProperty(value = "游戏包名")
    private String packageName;

    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    private String autograph;

    /**
     * 上架时间
     */
    @ApiModelProperty(value = "上架时间")
    private Date onTime;

    /**
     * 下架时间
     */
    @ApiModelProperty(value = "下架时间")
    private Date offTime;

    /**
     * ISBN核发单
     */
    @ApiModelProperty(value = "ISBN核发单")
    private String isbnIssuanceOrder;

    /**
     * ISBN号
     */
    @ApiModelProperty(value = "ISBN号")
    private String isbnNumber;

    /**
     * 著作权证
     */
    @ApiModelProperty(value = "著作权证")
    private String copyrightUrl;

    /**
     * 授权链
     */
    @ApiModelProperty(value = "授权链")
    private String authorizationChain;

    /**
     * 其他资质
     */
    @ApiModelProperty(value = "其他资质")
    private String otherQualifications;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 删除状态(0 正常  1删除)
     */
    @ApiModelProperty(value = "删除状态(0 正常  1删除)")
    private Integer delFlag;

    /**
     * universal_link
     */
    @ApiModelProperty("universal_link")
    private String universalLink;

    /**
     * url_scheme
     */
    @ApiModelProperty("url_scheme")
    private String urlScheme;

    /**
     * 备案识别码
     */
    @ApiModelProperty(value = "备案识别码")
    private Long bizId;

}
