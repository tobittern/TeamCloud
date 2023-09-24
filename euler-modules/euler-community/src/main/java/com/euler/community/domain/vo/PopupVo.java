package com.euler.community.domain.vo;

import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 弹窗管理视图对象 popup
 *
 * @author euler
 * @date 2022-06-02
 */
@Data
@ApiModel("弹窗管理视图对象")
@ExcelIgnoreUnannotated
public class PopupVo {

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
     * 专题名称
     */
    @ExcelProperty(value = "专题名称")
    @ApiModelProperty("专题名称")
    private String title;

    /**
     * 每天显示次数
     */
    @ExcelProperty(value = "每天显示次数")
    @ApiModelProperty("每天显示次数")
    private Integer times;

    /**
     * 弹窗显示开始时间
     */
    @ExcelProperty(value = "弹窗显示开始时间")
    @ApiModelProperty("弹窗显示开始时间")
    private Date startTime;

    /**
     * 弹窗显示结束时间
     */
    @ExcelProperty(value = "弹窗显示结束时间")
    @ApiModelProperty("弹窗显示结束时间")
    private Date endTime;

    /**
     * 弹窗显示时间
     */
    @ExcelProperty(value = "弹窗显示时间")
    @ApiModelProperty("弹窗显示时间")
    private String showTime;

    /**
     * 弹窗图片
     */
    @ExcelProperty(value = "弹窗图片")
    @ApiModelProperty("弹窗图片")
    private String popupIcon;

    /**
     * 跳转url
     */
    @ExcelProperty(value = "跳转url")
    @ApiModelProperty("跳转url")
    private String jumpUrl;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ExcelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高")
    @ApiModelProperty("显示优先级，默认值0，数字越小，显示级别越高")
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    @ExcelProperty(value = "弹窗状态，0待启用，1已启用，2已停用")
    @ApiModelProperty("弹窗状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单
     */
    @ExcelProperty(value = "弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单")
    @ApiModelProperty("弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单")
    private String position;

}
