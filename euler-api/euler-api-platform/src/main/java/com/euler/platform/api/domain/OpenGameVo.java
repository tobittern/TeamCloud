package com.euler.platform.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 【游戏管理】视图对象 open_game
 *
 * @author open
 * @date 2022-02-18
 */
@Data
@ApiModel("【游戏管理】视图对象")
@ExcelIgnoreUnannotated
public class OpenGameVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    @ExcelProperty(value = "主键自增")
    @ApiModelProperty("主键自增")
    private Integer id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 对应版本的ID
     */
    @ExcelProperty(value = "对应版本的ID")
    @ApiModelProperty("对应版本的ID")
    private Integer versionId;

    /**
     * 游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)
     */
    @ExcelProperty(value = "游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)")
    @ApiModelProperty("游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)")
    private Integer gameStatus;

    /**
     * 游戏上架状态(0待上线  1上线  2下线)
     */
    @ExcelProperty(value = "游戏上架状态(0待上线  1上线  2下线)")
    @ApiModelProperty(value = "游戏上架状态(0待上线  1上线  2下线)")
    private Integer onlineStatus;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 游戏类目
     */
    @ExcelProperty(value = "游戏类目")
    @ApiModelProperty("游戏类目")
    private String gameCategory;

    /**
     * 游戏类目名称
     */
    @ExcelProperty(value = "游戏类目名称")
    @ApiModelProperty("游戏类目名称")
    private String gameCategoryName;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ExcelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    @ApiModelProperty("运行平台 (1 安卓  2 ios  3 h5)")
    private Integer operationPlatform;

    /**
     * 充值回调
     */
    @ExcelProperty(value = "充值回调")
    @ApiModelProperty("充值回调")
    private String rechargeCallback;

    /**
     * 回调秘钥
     */
    @ExcelProperty(value = "回调秘钥")
    @ApiModelProperty("回调秘钥")
    private String callbackSecretKey;

    /**
     * icon图片地址
     */
    @ExcelProperty(value = "icon图片地址")
    @ApiModelProperty("icon图片地址")
    private String iconUrl;

    /**
     * 游戏的图片列表
     */
    @ExcelProperty(value = "游戏的图片列表")
    @ApiModelProperty("游戏的图片列表")
    private String pictureUrl;

    /**
     * 游戏简介
     */
    @ExcelProperty(value = "游戏简介")
    @ApiModelProperty("游戏简介")
    private String gameIntroduction;

    /**
     * 付费类型(1 有付费 2无付费)
     */
    @ExcelProperty(value = "付费类型(1 有付费 2无付费)")
    @ApiModelProperty("付费类型(1 有付费 2无付费)")
    private Integer payType;

    /**
     * 游戏标签 多个用逗号隔开
     */
    @ExcelProperty(value = "游戏标签 多个用逗号隔开")
    @ApiModelProperty("游戏标签 多个用逗号隔开")
    private String gameTags;

    /**
     * 游戏的安装包(运行平台安卓 ios的时候必须存在)
     */
    @ExcelProperty(value = "游戏的安装包(运行平台安卓 ios的时候必须存在)")
    @ApiModelProperty("游戏的安装包(运行平台安卓 ios的时候必须存在)")
    private String gameInstallPackage;

    /**
     * 游戏的版本号
     */
    @ExcelProperty(value = "游戏的版本号")
    @ApiModelProperty("游戏的版本号")
    private String versionNumberName;

    /**
     * 游戏包名
     */
    @ExcelProperty(value = "游戏包名")
    @ApiModelProperty("游戏包名")
    private String packageName;

    /**
     * 签名
     */
    @ExcelProperty(value = "签名")
    @ApiModelProperty("签名")
    private String autograph;

    /**
     * 上架时间
     */
    @ExcelProperty(value = "上架时间")
    @ApiModelProperty("上架时间")
    private Date onTime;

    /**
     * 下架时间
     */
    @ExcelProperty(value = "下架时间")
    @ApiModelProperty("下架时间")
    private Date offTime;

    /**
     * ISBN核发单
     */
    @ExcelProperty(value = "ISBN核发单")
    @ApiModelProperty("ISBN核发单")
    private String isbnIssuanceOrder;

    /**
     * ISBN号
     */
    @ExcelProperty(value = "ISBN号")
    @ApiModelProperty("ISBN号")
    private String isbnNumber;

    /**
     * 著作权证
     */
    @ExcelProperty(value = "著作权证")
    @ApiModelProperty("著作权证")
    private String copyrightUrl;

    /**
     * 授权链
     */
    @ExcelProperty(value = "授权链")
    @ApiModelProperty("授权链")
    private String authorizationChain;

    /**
     * 其他资质
     */
    @ExcelProperty(value = "其他资质")
    @ApiModelProperty("其他资质")
    private String otherQualifications;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    @ApiModelProperty("审核时间")
    private Date auditTime;

    /**
     * 审核原因
     */
    @ExcelProperty(value = "审核原因")
    @ApiModelProperty("审核原因")
    private String auditRecord;

    /**
     * app_id
     */
    @ExcelProperty(value = "app_id")
    @ApiModelProperty("app_id")
    private String appId;

    /**
     * app_secret
     */
    @ExcelProperty(value = "app_secret")
    @ApiModelProperty("app_secret")
    private String appSecret;

    /**
     * 其他资质
     */
    @ExcelProperty(value = "创建者")
    @ApiModelProperty("创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * universal_link
     */
    @ExcelProperty(value = "universal_link")
    @ApiModelProperty("universal_link")
    private String universalLink;

    /**
     * url_scheme
     */
    @ExcelProperty(value = "url_scheme")
    @ApiModelProperty("url_scheme")
    private String urlScheme;

    /**
     * 版本历史库中的版本号
     */
    @ExcelProperty(value = "版本历史库中的版本号")
    @ApiModelProperty("版本历史库中的版本号")
    private String versionHistoryNumber;

    /**
     * 版本的状态
     */
    @ExcelProperty(value = "版本的状态")
    @ApiModelProperty("版本的状态")
    private Integer versionHistoryStatus;

    /**
     * 版本的创建时间
     */
    @ExcelProperty(value = "版本的创建时间")
    @ApiModelProperty("版本的创建时间")
    private Date versionHistoryCreateTime;

    /**
     * 版本上线时间
     */
    @ExcelProperty(value = "版本上线时间")
    @ApiModelProperty("版本上线时间")
    private Date versionOnlineTime;

    /**
     * 所属公司
     */
    private String companyName;



    private GameVersionHistoryVo gameVersionHistoryVo;

}
