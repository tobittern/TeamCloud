package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 渠道分组视图对象 channel_group
 *
 * @author euler
 * @date 2022-04-01
 */
@Data
@ApiModel("渠道分组视图对象")
@ExcelIgnoreUnannotated
public class ChannelPackageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 主渠道ID
     */
    @ExcelProperty(value = "主渠道ID")
    @ApiModelProperty("主渠道ID")
    private Integer channelId;

    /**
     * 主渠道名
     */
    @ExcelProperty(value = "主渠道名")
    @ApiModelProperty("主渠道名")
    private String channelName;

    /**
     * 对应游戏ID
     */
    @ExcelProperty(value = "对应游戏ID")
    @ApiModelProperty("对应游戏ID")
    private Integer gameId = 0;

    /**
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;



    /**
     * 新游戏名
     */
    @ExcelProperty(value = "新游戏名")
    @ApiModelProperty("新游戏名")
    private String newGameName;

    /**
     * 分包的icon
     */
    @ExcelProperty(value = "分包的icon")
    @ApiModelProperty("分包的icon")
    private String icon;

    /**
     * 分包的前缀名称
     */
    @ExcelProperty(value = "分包的前缀名称")
    @ApiModelProperty("分包的前缀名称")
    private String packagePrefixName;

    /**
     * 分包的名称
     */
    @ExcelProperty(value = "分包的名称")
    @ApiModelProperty("分包的名称")
    private String packageCode;

    /**
     * 分包的标签
     */
    @ExcelProperty(value = "分包的标签")
    @ApiModelProperty("分包的标签")
    private String label;

    /**
     * 版本ID
     */
    @ExcelProperty(value = "版本ID")
    @ApiModelProperty("版本ID")
    private Integer versionId;

    /**
     * 版本
     */
    @ExcelProperty(value = "版本")
    @ApiModelProperty("版本")
    private String version;

    /**
     * 分包标识符
     */
    @ExcelProperty(value = "分包标识符")
    @ApiModelProperty("分包标识符")
    private String edition;

    /**
     * 包地址
     */
    @ExcelProperty(value = "分保渠道版本 其实值得就是次数")
    @ApiModelProperty("分保渠道版本 其实值得就是次数")
    private String packageAddress;

    /**
     * 下载地址
     */
    @ExcelProperty(value = "下载地址")
    @ApiModelProperty("下载地址")
    private String realDownAddress;

    /**
     * 是否进行分包 0需要进行分包 1分包完毕
     */
    @ExcelProperty(value = "是否进行分包 0需要进行分包 1分包完毕")
    @ApiModelProperty("是否进行分包 0需要进行分包 1分包完毕")
    private Integer status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


    /**
     * 打包类型  1APP
     */
    @ExcelProperty(value = "打包类型")
    @ApiModelProperty("打包类型")
    private Integer packageType;

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
