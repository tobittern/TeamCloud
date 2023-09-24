package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 关注用户视图对象 attention_user
 *
 * @author euler
 * @date 2022-07-07
 */
@Data
@ApiModel("关注用户视图对象")
@ExcelIgnoreUnannotated
public class UserExtendVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 签到时间
     */
    @ExcelProperty(value = "签到时间")
    @ApiModelProperty("签到时间")
    private Date signDate;

    /**
     * 是否是官方账号  0不是  1是
     */
    @ExcelProperty(value = "是否是官方账号")
    @ApiModelProperty("是否是官方账号")
    private Integer isOfficial;

    /**
     * 性别 1男 0女 2未知
     */
    @ExcelProperty(value = "性别 1男 0女 2未知")
    @ApiModelProperty("性别 1男 0女 2未知")
    private String sex;

}
