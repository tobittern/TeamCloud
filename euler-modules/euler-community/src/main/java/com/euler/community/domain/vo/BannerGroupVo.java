package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * banner组视图对象 banner_group
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@ApiModel("banner组视图对象")
@ExcelIgnoreUnannotated
public class BannerGroupVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 应用场景，0发现菜单，1个人中心
     */
    @ExcelProperty(value = "应用场景，0发现菜单，1个人中心")
    @ApiModelProperty("应用场景，0发现菜单，1个人中心")
    private String applicationType;

    /**
     * banner组名
     */
    @ExcelProperty(value = "banner组名")
    @ApiModelProperty("banner组名")
    private String groupName;

    /**
     * 显示开始时间
     */
    @ExcelProperty(value = "显示开始时间")
    @ApiModelProperty("显示开始时间")
    private Date startTime;

    /**
     * 显示结束时间
     */
    @ExcelProperty(value = "显示结束时间")
    @ApiModelProperty("显示结束时间")
    private Date endTime;

    /**
     * bannerGroup显示时间
     */
    @ExcelProperty(value = "bannerGroup显示时间")
    @ApiModelProperty("bannerGroup显示时间")
    private String showTime;

    /**
     * banner内容，格式为json格式
     */
    @ExcelProperty(value = "banner内容")
    @ApiModelProperty("banner内容")
    private String bannerContent;

    /**
     * banner内容，格式为json格式
     */
    @ExcelProperty(value = "banner内容，格式为json格式")
    @ApiModelProperty("banner内容，格式为json格式")
    private List<BannerVo> showBannerContent;

    /**
     * banner数量
     */
    @ExcelProperty(value = "banner数量")
    @ApiModelProperty("banner数量")
    private Integer bannerNumber;

    /**
     * 状态，0待启用，1已启用，2已停用
     */
    @ExcelProperty(value = "状态，0待启用，1已启用，2已停用")
    @ApiModelProperty("状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * banner图
     */
    @ExcelProperty(value = "banner图")
    @ApiModelProperty("banner图")
    private String bannerIcon;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
