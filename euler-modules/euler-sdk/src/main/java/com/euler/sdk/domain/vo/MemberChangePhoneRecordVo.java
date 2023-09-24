package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户更换手机号记录视图对象 member_change_phone_record
 *
 * @author euler
 * @date 2022-06-13
 */
@Data
@ApiModel("用户更换手机号记录视图对象")
@ExcelIgnoreUnannotated
public class MemberChangePhoneRecordVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ExcelProperty(value = "自增主键")
    @ApiModelProperty("自增主键")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long memberId;

    /**
     * 原始手机号码
     */
    @ExcelProperty(value = "原始手机号码")
    @ApiModelProperty("原始手机号码")
    private String originalMobile;

    /**
     * 新手机号
     */
    @ExcelProperty(value = "新手机号")
    @ApiModelProperty("新手机号")
    private String newMobile;


}
