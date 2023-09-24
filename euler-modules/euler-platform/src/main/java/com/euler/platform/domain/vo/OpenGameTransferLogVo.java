package com.euler.platform.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 开放平台游戏转移记录视图对象 open_game_transfer_log
 *
 * @author euler
 * @date 2022-07-25
 */
@Data
@ApiModel("开放平台游戏转移记录视图对象")
@ExcelIgnoreUnannotated
public class OpenGameTransferLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏ID
     */
    @ExcelProperty(value = "游戏ID")
    @ApiModelProperty("游戏ID")
    private Integer gameId;

    /**
     * 原始拥有的开放平台认证用户id
     */
    @ExcelProperty(value = "原始拥有的开放平台认证用户id")
    @ApiModelProperty("原始拥有的开放平台认证用户id")
    private Long originalOpid;

    /**
     * 原始拥有者账号名
     */
    @ExcelProperty(value = "原始拥有者账号名")
    @ApiModelProperty("原始拥有者账号名")
    private String originalUsername;

    /**
     * 原始公司名
     */
    @ExcelProperty(value = "原始公司名")
    @ApiModelProperty("原始公司名")
    private String originalCompanyName;

    /**
     * 转移给的开放平台认证用户id
     */
    @ExcelProperty(value = "转移给的开放平台认证用户id")
    @ApiModelProperty("转移给的开放平台认证用户id")
    private Long transferOpid;

    /**
     * 转移给的公司名
     */
    @ExcelProperty(value = "转移给的公司名")
    @ApiModelProperty("转移给的公司名")
    private String transferCompanyName;

    /**
     * 转移之后的账号名
     */
    @ExcelProperty(value = "转移之后的账号名")
    @ApiModelProperty("转移之后的账号名")
    private String transferUsername;


}
