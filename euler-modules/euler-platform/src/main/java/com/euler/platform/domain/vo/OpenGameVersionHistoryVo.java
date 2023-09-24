package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 游戏版本历史视图对象 open_game_version_history
 *
 * @author euler
 * @date 2022-03-31
 */
@Data
@ApiModel("游戏版本历史视图对象")
@ExcelIgnoreUnannotated
public class OpenGameVersionHistoryVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 审核状态 (0 初始状态 1审核中  2审核成功  3审核失败)
     */
    @ExcelProperty(value = "审核状态 (0 初始状态 1审核中  2审核成功  3审核失败)")
    @ApiModelProperty("审核状态 (0 初始状态 1审核中  2审核成功  3审核失败)")
    private Integer auditStatus;


    /**
     * 版本号
     */
    @ExcelProperty(value = "版本号")
    @ApiModelProperty("版本号")
    private Integer versionNumber;

    /**
     * 版本号名称
     */
    @ExcelProperty(value = "版本号名称")
    @ApiModelProperty("版本号名称")
    private String versionNumberName;

    /**
     * 版本说明
     */
    @ExcelProperty(value = "版本说明")
    @ApiModelProperty("版本说明")
    private String versionDescription;

    /**
     * 游戏的图片列表
     */
    @ExcelProperty(value = "游戏的图片列表")
    @ApiModelProperty("游戏的图片列表")
    private String pictureUrl;

    /**
     * 游戏安装包地址
     */
    @ExcelProperty(value = "游戏安装包地址")
    @ApiModelProperty("游戏安装包地址")
    private String gameInstallPackage;

    /**
     * 是否上架
     */
    @ExcelProperty(value = "是否上架")
    @ApiModelProperty("是否上架")
    private Integer isUp = 0;

    /**
     * 审核时间
     */
    @ExcelProperty(value = "审核时间")
    @ApiModelProperty("审核时间")
    private Date auditTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
