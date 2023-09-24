package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * 广告媒体视图对象 advertising_media
 *
 * @author euler
 * @date 2022-09-22
 */
@Data
@ApiModel("广告媒体视图对象")
@ExcelIgnoreUnannotated
public class AdvertisingMediaVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 媒体名称
     */
    @ExcelProperty(value = "媒体名称")
    @ApiModelProperty("媒体名称")
    private String mediaName;



    /**
     * 广告平台
     */
    @ExcelProperty(value = "广告平台 ")
    @ApiModelProperty("广告平台 ")
    private String advertisingPlatform;

    /**
     * 返点比例
     */
    @ExcelProperty(value = "返点比例")
    @ApiModelProperty("返点比例")
    private Integer rebate;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
