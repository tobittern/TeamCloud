package com.euler.risk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 封号列视图对象 banlist
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@ApiModel("封号列视图对象")
@ExcelIgnoreUnannotated
public class SearchBanVo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ExcelProperty(value = "自增主键")
    @ApiModelProperty("自增主键")
    private Integer id;

    /**
     * 平台1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    @ExcelProperty(value = "平台1：sdk，2：开放平台，3：管理后台 4：九区玩家APP")
    @ApiModelProperty("平台1：sdk，2：开放平台，3：管理后台 4：九区玩家APP")
    private String platform;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long memberId;

    /**
     * 账号
     */
    @ExcelProperty(value = "账号")
    @ApiModelProperty("账号")
    private String account;
    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 游戏ID
     */
    @ExcelProperty(value = "游戏ID")
    @ApiModelProperty("游戏ID")
    private Integer gameId;

    /**
     * 封号开始时间
     */
    @ExcelProperty(value = "封号开始时间")
    @ApiModelProperty("封号开始时间")
    private Date startTime;

    /**
     * 封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁
     */
    @ExcelProperty(value = "封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁")
    @ApiModelProperty("封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁")
    private Date endTime;

    /**
     * 封号类型
     */
    @ExcelProperty(value = "封号类型")
    @ApiModelProperty("封号类型")
    private String banType;

    /**
     * 封号原因
     */
    @ExcelProperty(value = "封号原因")
    @ApiModelProperty("封号原因")
    private String reason;

    /**
     * 封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备
     */
    @ExcelProperty(value = "封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备")
    @ApiModelProperty("封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备")
    private Integer searchType;


}
