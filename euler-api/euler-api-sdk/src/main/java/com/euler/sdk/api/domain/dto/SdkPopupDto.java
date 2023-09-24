package com.euler.sdk.api.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 我的游戏视图对象 my_game
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@ApiModel("我的游戏视图对象")
@ExcelIgnoreUnannotated
public class SdkPopupDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 游戏ID
     */
    @ExcelProperty(value = "游戏ID")
    @ApiModelProperty("游戏ID")
    private Integer gameId;

    /**
     * 渠道号
     */
    @ExcelProperty(value = "渠道号")
    @ApiModelProperty("渠道号")
    private String packageCode;

    /**
     * 游戏区服
     */
    @ExcelProperty(value = "游戏区服")
    @ApiModelProperty("游戏区服")
    private String gameServerId;

    /**
     * 是否是第一次
     */
    @ExcelProperty(value = "是否是第一次")
    @ApiModelProperty("是否是第一次")
    private Integer isFirst = 0;

}
