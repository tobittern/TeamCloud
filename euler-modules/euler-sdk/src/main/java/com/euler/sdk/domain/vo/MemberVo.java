package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户信息视图对象 member
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@ApiModel("用户信息视图对象")
@ExcelIgnoreUnannotated
public class MemberVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long id;

    /**
     * 全网唯一标识
     */
    @ExcelProperty(value = "全网唯一标识")
    @ApiModelProperty("全网唯一标识")
    private String uniqueId;

    /**
     * 用户账号
     */
    @ExcelProperty(value = "用户账号")
    @ApiModelProperty("用户账号")
    private String account;

    /**
     * 手机号码
     */
    @ExcelProperty(value = "手机号码")
    @ApiModelProperty("手机号码")
    private String mobile;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    @ApiModelProperty("密码")
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty(value = "帐号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    @ApiModelProperty("帐号状态（0正常 1停用）")
    private String status;


}
