package com.euler.payment.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;


/**
 * 用户与商家之间的协商记录视图对象 ali_complaint_reply_detail_infos
 *
 * @author euler
 * @date 2022-10-18
 */
@Data
@ApiModel("用户与商家之间的协商记录视图对象")
@ExcelIgnoreUnannotated
public class AliComplaintReplyDetailInfosVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 支付宝侧投诉单号
     */
    @ExcelProperty(value = "支付宝侧投诉单号")
    @ApiModelProperty("支付宝侧投诉单号")
    private String complainEventId;

    /**
     * 回复人名称
     */
    @ExcelProperty(value = "回复人名称")
    @ApiModelProperty("回复人名称")
    private String replierName;

    /**
     * 用户角色
     */
    @ExcelProperty(value = "用户角色")
    @ApiModelProperty("用户角色")
    private String replierRole;

    /**
     * 回复时间
     */
    @ExcelProperty(value = "回复时间")
    @ApiModelProperty("回复时间")
    private String gmtCreate;

    /**
     * 回复内容
     */
    @ExcelProperty(value = "回复内容")
    @ApiModelProperty("回复内容")
    private String content;

    /**
     * 回复图片
     */
    @ExcelProperty(value = "回复图片")
    @ApiModelProperty("回复图片")
    private List<String> images;

    /**
     * 回复图片
     */
    @ExcelProperty(value = "回复图片")
    @ApiModelProperty("回复图片")
    private String imagesString;


}
