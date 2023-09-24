package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class MemberBlacklistVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ExcelProperty(value = "自增主键")
    @ApiModelProperty("自增主键")
    private Integer id;
    /**
     * 平台 0全平台 1sdk  2app
     */
    @ExcelProperty(value = "平台")
    @ApiModelProperty("平台")
    private Integer platform;
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
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;

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
     * ip地址
     */
    @ExcelProperty(value = "ip地址")
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * 类型 1用户 2IP
     */
    @ExcelProperty(value = "类型 1用户 2IP")
    @ApiModelProperty("类型 1用户 2IP")
    private Integer type;


}
