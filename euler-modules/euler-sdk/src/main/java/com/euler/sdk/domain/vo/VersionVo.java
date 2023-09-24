package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * sdk版本管理视图对象 version
 *
 * @author euler
 * @date 2022-07-08
 */
@Data
@ApiModel("sdk版本管理视图对象")
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
     * 版本号
     */
    @ExcelProperty(value = "版本号")
    @ApiModelProperty("版本号")
    private String number;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ExcelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    @ApiModelProperty("运行平台 (1 安卓  2 ios  3 h5)")
    private Integer platform;

    /**
     * 版本类型，0稳定版，1最新版
     */
    @ExcelProperty(value = "版本类型，0稳定版，1最新版")
    @ApiModelProperty("版本类型，0稳定版，1最新版")
    private Integer type;

    /**
     * 是否是新版本，0是新版本，1历史版本
     */
    @ExcelProperty(value = "是否是新版本，0是新版本，1历史版本")
    @ApiModelProperty("是否是新版本，0是新版本，1历史版本")
    private Integer isNew;

    /**
     * 更新内容
     */
    @ExcelProperty(value = "更新内容")
    @ApiModelProperty("更新内容")
    private String content;

    /**
     * 文件路径
     */
    @ExcelProperty(value = "文件路径")
    @ApiModelProperty("文件路径")
    private String fileUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 返显时间1
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date date1;


    /**
     * 返显时间2
     */
    @JsonFormat(pattern="yyyy/MM/dd")
    private Date date2;


}
