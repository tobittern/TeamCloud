package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视图对象 sign_in
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class SignInVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 签到用户
     */
    @ExcelProperty(value = "签到用户")
    @ApiModelProperty("签到用户")
    private Long memberId;

    /**
     * 签到活动ID
     */
    @ExcelProperty(value = "签到活动ID")
    @ApiModelProperty("签到活动ID")
    private Integer activeId;

    /**
     * 签到日期
     */
    @ExcelProperty(value = "签到日期")
    @ApiModelProperty("签到日期")
    private Integer week;

    /**
     * 积分数
     */
    @ExcelProperty(value = "积分数")
    @ApiModelProperty("积分数")
    private Integer score;


}
