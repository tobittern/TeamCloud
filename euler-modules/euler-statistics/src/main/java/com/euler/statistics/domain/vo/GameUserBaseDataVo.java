package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@ApiModel("游戏用户管理视图对象")
@ExcelIgnoreUnannotated
public class GameUserBaseDataVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 渠道code和游戏id关联字段
     */
    @ExcelProperty(value = "渠道code和游戏id关联字段")
    @ApiModelProperty("渠道code和游戏id关联字段")
    private String selectKey;

    /**
     * 角色id
     */
    @ExcelProperty(value = "角色id")
    @ApiModelProperty("角色id")
    private String roleId;

    /**
     * 服务器id
     */
    @ExcelProperty(value = "服务器id")
    @ApiModelProperty("服务器id")
    private String serverId;

    /**
     * 主渠道id
     */
    @ExcelProperty(value = "主渠道id")
    @ApiModelProperty("主渠道id")
    private Integer channelId;

    /**
     * 主渠道name
     */
    @ExcelProperty(value = "主渠道name")
    @ApiModelProperty("主渠道name")
    private String channelName;


    /**
     * 渠道code
     */
    @ExcelProperty(value = "渠道code")
    @ApiModelProperty("渠道code")
    private String packageCode;

    /**
     * 游戏id
     */
    @ExcelProperty(value = "游戏id")
    @ApiModelProperty("游戏id")
    private Integer gameId;

    /**
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ExcelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    @ApiModelProperty("运行平台 (1 安卓  2 ios  3 h5)")
    private Integer operationPlatform;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    @ApiModelProperty("用户ID")
    private Long memberId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


    /**
     * 注册时间
     */
    @ExcelProperty(value = "注册时间")
    @ApiModelProperty("注册时间")
    private Date registerTime;

    /**
     * 日期标签
     */
    @ExcelProperty(value = "日期标签")
    @ApiModelProperty("日期标签")
    private String dateLabel;



}
