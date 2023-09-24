package com.euler.community.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 版本管理视图对象 version
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("版本管理视图对象")
@ExcelIgnoreUnannotated
public class VersionVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    @ApiModelProperty("内容")
    private String content;

    /**
     * 应用系统，'1': android  '2': ios
     */
    @ExcelProperty(value = "应用系统，'1': android  '2': ios")
    @ApiModelProperty("应用系统，'1': android  '2': ios")
    private String systemType;

    /**
     * 版本号
     */
    @ExcelProperty(value = "版本号")
    @ApiModelProperty(value = "版本号")
    private String versionNo;

    /**
     * ios的时候，下载地址
     */
    @ExcelProperty(value = "ios的时候，下载地址")
    @ApiModelProperty(value = "ios的时候，下载地址")
    private String downloadUrl;

    /**
     * 附件名称
     */
    @ExcelProperty(value = "附件名称")
    @ApiModelProperty("附件名称")
    private String fileName;

    /**
     * 附件路径
     */
    @ExcelProperty(value = "附件路径")
    @ApiModelProperty("附件路径")
    private String filePath;

    /**
     * 上传时间
     */
    @ExcelProperty(value = "上传时间")
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 发布时间
     */
    @ExcelProperty(value = "发布时间")
    @ApiModelProperty("发布时间")
    private Date publishTime;

    /**
     * 更新方式，'1':推荐更新 '2':强制更新
     */
    @ExcelProperty(value = "更新方式，'1':推荐更新 '2':强制更新")
    @ApiModelProperty("更新方式，'1':推荐更新 '2':强制更新")
    private String updateType;

    /**
     * 版本状态，'1':待发布 '2':已发布 '3':已下架
     */
    @ExcelProperty(value = "版本状态，'1':待发布 '2':已发布 '3':已下架")
    @ApiModelProperty("版本状态，'1':待发布 '2':已发布 '3':已下架")
    private String versionStatus;

    /**
     * 创建者
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


    @ApiModelProperty("发现页开关：1：开，0：关")
    private String discoverSwitch;
}
