package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 礼包配置视图对象 gift_bag
 *
 * @author euler
 * @date 2022-06-02
 */
@Data
@ApiModel("礼包配置视图对象")
@ExcelIgnoreUnannotated
public class GiftBagVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Long gameId;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 礼包名称
     */
    @ExcelProperty(value = "礼包名称")
    @ApiModelProperty("礼包名称")
    private String giftName;

    /**
     * 激活码
     */
    @ExcelProperty(value = "激活码")
    @ApiModelProperty("激活码")
    private String activationCode;

    /**
     * 礼包图片名称
     */
    @ExcelProperty(value = "礼包图片名称")
    @ApiModelProperty("礼包图片名称")
    private String picName;

    /**
     * 礼包图片路径
     */
    @ExcelProperty(value = "礼包图片路径")
    @ApiModelProperty("礼包图片路径")
    private String picPath;

    /**
     * 礼包内容
     */
    @ExcelProperty(value = "礼包内容")
    @ApiModelProperty("礼包内容")
    private String content;

    /**
     * 使用方式
     */
    @ExcelProperty(value = "使用方式")
    @ApiModelProperty("使用方式")
    private String useMethod;

    /**
     * 有效期-开始时间
     */
    @ExcelProperty(value = "有效期-开始时间")
    @ApiModelProperty("有效期-开始时间")
    private Date startTime;

    /**
     * 有效期-结束时间
     */
    @ExcelProperty(value = "有效期-结束时间")
    @ApiModelProperty("有效期-结束时间")
    private Date endTime;

    /**
     * 礼包总数
     */
    @ExcelProperty(value = "礼包总数")
    @ApiModelProperty("礼包总数")
    private Integer totalNum;

    /**
     * 礼包领取数目
     */
    @ExcelProperty(value = "礼包领取数目")
    @ApiModelProperty("礼包领取数目")
    private Integer drawNum;

    /**
     * 礼包兑换数目
     */
    @ExcelProperty(value = "礼包兑换数目")
    @ApiModelProperty("礼包兑换数目")
    private Integer exchangeNum;

    /**
     * 状态，0已上架吧，1已下架
     */
    @ExcelProperty(value = "状态，0已上架吧，1已下架")
    @ApiModelProperty("状态，0已上架吧，1已下架")
    private Integer status;

    /**
     * 资源表主键id,用于附件下载
     */
    @ExcelProperty(value = "资源表主键id,用于附件下载")
    @ApiModelProperty("资源表主键id,用于附件下载")
    private Long ossId;

    /**
     * cdk礼包文件名称
     */
    @ExcelProperty(value = "cdk礼包文件名称")
    @ApiModelProperty("cdk礼包文件名称")
    private String cdkFileName;

    /**
     * cdk礼包文件路径
     */
    @ExcelProperty(value = "cdk礼包文件路径")
    @ApiModelProperty("cdk礼包文件路径")
    private String cdkFilePath;

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
     * 版本上线时间
     */
    @ExcelProperty(value = "版本上线时间")
    @ApiModelProperty("版本上线时间")
    private Date versionOnlineTime;

    /**
     * 是否已领取 "0"未领取 "1"已领取 "2"不可领取
     */
    @ExcelProperty(value = "是否已领取 0未领取 1已领取 2不可领取")
    @ApiModelProperty("是否已领取 0未领取 1已领取 2不可领取")
    private String isPicked = "0";

    /**
     * 礼包兑换期限
     */
    @ExcelProperty(value = "礼包兑换期限")
    @ApiModelProperty("礼包兑换期限")
    private String showTime;

    /**
     * 应用平台 1：android 2：ios 3：h5
     */
    @ExcelProperty(value = "应用平台")
    @ApiModelProperty("应用平台")
    private String applicationType;

    /**
     * ios下载地址
     */
    @ExcelProperty(value = "ios下载地址")
    @ApiModelProperty("ios下载地址")
    private String iosDownloadUrl;

    /**
     * android下载地址
     */
    @ExcelProperty(value = "android下载地址")
    @ApiModelProperty("android下载地址")
    private String androidDownloadUrl;

    /**
     * h5下载地址
     */
    @ExcelProperty(value = "h5下载地址")
    @ApiModelProperty("h5下载地址")
    private String h5DownloadUrl;

}
