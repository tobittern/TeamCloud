package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * banner列视图对象 banner_list
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("banner列视图对象")
@ExcelIgnoreUnannotated
public class BannerVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * banner名
     */
    @ExcelProperty(value = "banner名")
    @ApiModelProperty("banner名")
    private String bannerName;

    /**
     * banner图
     */
    @ExcelProperty(value = "banner图")
    @ApiModelProperty("banner图")
    private String bannerIcon;

    /**
     * 跳转路径
     */
    @ExcelProperty(value = "跳转路径")
    @ApiModelProperty("跳转路径")
    private String jumpUrl;

}
