package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发现配置视图对象 discover
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("发现配置视图对象 -- 游戏")
@ExcelIgnoreUnannotated
public class DiscoverShowGameVo {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏ID
     */
    @ExcelProperty(value = "游戏ID")
    @ApiModelProperty("游戏ID")
    private Integer gameId;

    /**
     * 下载地址
     */
    @ExcelProperty(value = "下载地址")
    @ApiModelProperty("下载地址")
    private String downloadAddress;

    /**
     * 标签
     */
    @ExcelProperty(value = "标签")
    @ApiModelProperty("标签")
    private String tag;

    /**
     * 游戏Icon
     */
    @ExcelProperty(value = "游戏Icon")
    @ApiModelProperty("游戏Icon")
    private String gameIcon;

    /**
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;

    /**
     * 游戏分类名
     */
    @ExcelProperty(value = "游戏分类名")
    @ApiModelProperty("游戏分类名")
    private String gameCategoryName;

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
     * 包名
     */
    @ExcelProperty(value = "包名")
    @ApiModelProperty("包名")
    private String packageName;

    /**
     * 启动类
     */
    @ExcelProperty(value = "启动类")
    @ApiModelProperty("启动类")
    private String startupClass;

}
