package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


/**
 * 投诉回复视图对象 complaint_response
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉回复视图对象")
@ExcelIgnoreUnannotated
public class ComplaintResponseVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 投诉单号
     */
    @ExcelProperty(value = "投诉单号")
    @ApiModelProperty("投诉单号")
    private String complaintId;

    /**
     * 回复内容
     */
    @ExcelProperty(value = "回复内容")
    @ApiModelProperty("回复内容")
    private String responseContent;

    /**
     * 回复图片 传输给微信的是一个数组
     */
    @ExcelProperty(value = "回复图片 传输给微信的是一个数组")
    @ApiModelProperty("回复图片 传输给微信的是一个数组")
    private String responseImages;

    /**
     * 跳转链接
     */
    @ExcelProperty(value = "跳转链接")
    @ApiModelProperty("跳转链接")
    private String jumpUrl;

    /**
     * 跳转链接文案
     */
    @ExcelProperty(value = "跳转链接文案")
    @ApiModelProperty("跳转链接文案")
    private String jumpUrlText;


}
