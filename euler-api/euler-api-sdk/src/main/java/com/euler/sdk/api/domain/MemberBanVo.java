package com.euler.sdk.api.domain;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户黑名单列视图对象 member_blacklist
 *
 * @author euler
 * @date 2022-06-13
 */
@Data
@ApiModel("用户黑名单列视图对象")
@ExcelIgnoreUnannotated
public class MemberBanVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long memberId;

    /**
     * 游戏ID
     */
    @ExcelProperty(value = "游戏ID")
    @ApiModelProperty("游戏ID")
    private Integer gameId;

    /**
     * 封号截止时间
     */
    @ExcelProperty(value = "封号截止时间")
    @ApiModelProperty("封号截止时间")
    private Date endTime;

    /**
     * 封号原因
     */
    @ExcelProperty(value = "封号原因")
    @ApiModelProperty("封号原因")
    private String record;

    /**
     * 是否是永久封号  0 不是  1是
     */
    @ExcelProperty(value = "是否是永久封号  0 不是  1是")
    @ApiModelProperty("是否是永久封号  0 不是  1是")
    private Integer isPermanent;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    @ApiModelProperty("用户名")
    private String memberName;


}
