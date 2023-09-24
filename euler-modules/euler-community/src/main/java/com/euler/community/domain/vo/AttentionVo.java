package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关注视图对象 attention
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@ApiModel("关注视图对象")
@ExcelIgnoreUnannotated
public class AttentionVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 被关注用户id
     */
    @ExcelProperty(value = "被关注用户id")
    @ApiModelProperty("被关注用户id")
    private Long attentionUserId;

    /**
     * 状态 1:未关注 2:已关注 3:互相关注
     */
    @ExcelProperty(value = "状态 1:未关注 2:已关注 3:互相关注")
    @ApiModelProperty("状态 1:未关注 2:已关注 3:互相关注")
    private String status;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    @ApiModelProperty("头像")
    private String avatar;

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
