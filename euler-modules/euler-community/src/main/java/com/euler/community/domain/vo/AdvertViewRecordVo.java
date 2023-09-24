package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视图对象 advert_view_record
 *
 * @author euler
 * @date 2022-06-17
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class AdvertViewRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 广告表主键id
     */
    @ExcelProperty(value = "广告表主键id")
    @ApiModelProperty("广告表主键id")
    private Long advertId;

    /**
     * 查看广告的用户id
     */
    @ExcelProperty(value = "查看广告的用户id")
    @ApiModelProperty("查看广告的用户id")
    private Long memberId;

    /**
     * 查看广告的次数
     */
    @ExcelProperty(value = "查看广告的次数")
    @ApiModelProperty("查看广告的次数")
    private Integer viewNum;


}
