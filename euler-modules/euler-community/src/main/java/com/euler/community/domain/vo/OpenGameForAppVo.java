package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.platform.api.domain.GameVersionHistoryVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author open
 * @date 2022-02-18
 */
@Data
@ApiModel("【游戏管理】视图对象")
@ExcelIgnoreUnannotated
public class OpenGameForAppVo implements Serializable {

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
     * 游戏包名
     */
    @ExcelProperty(value = "游戏包名")
    @ApiModelProperty("游戏包名")
    private String packageName;

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

    @ExcelProperty(value = "下载地址")
    @ApiModelProperty("下载地址")
    private String downloadAddress;

    /**
     * 启动类
     */
    @ExcelProperty(value = "启动类")
    @ApiModelProperty("启动类")
    private String startupClass;

}
