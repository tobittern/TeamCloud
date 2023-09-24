package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * 设备账号信息视图对象 td_device_member
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@ApiModel("设备账号信息视图对象")
@ExcelIgnoreUnannotated
public class TdDeviceMemberVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 设备id
     */
    @ExcelProperty(value = "设备id")
    @ApiModelProperty("设备id")
    private String deviceId;

    /**
     * 账号
     */
    @ExcelProperty(value = "账号")
    @ApiModelProperty("账号")
    private String account;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 用户id，不登录为0
     */
    @ExcelProperty(value = "用户id，不登录为0")
    @ApiModelProperty("用户id，不登录为0")
    private Long userId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 封禁状态
     */
    @ExcelProperty(value = "封禁状态")
    @ApiModelProperty("封禁状态")
    private Integer banStatus = 0;

}
