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
 * 投诉资料列视图对象 complaint_media_list
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@ApiModel("投诉资料列视图对象")
@ExcelIgnoreUnannotated
public class ComplaintMediaListVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 媒体文件业务类型
     */
    @ExcelProperty(value = "媒体文件业务类型")
    @ApiModelProperty("媒体文件业务类型")
    private String mediaType;

    /**
     * 媒体文件请求url
     */
    @ExcelProperty(value = "媒体文件请求url")
    @ApiModelProperty("媒体文件请求url")
    private String mediaUrl;


}
