package com.euler.statistics.domain.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.euler.common.excel.annotation.ExcelDictFormat;
import com.euler.common.excel.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 视图对象 douyin_channel_aid_datas
 * @author euler
 * @date 2022-07-13
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class DouyinChannelAidDatasVo {

    private static final long serialVersionUID = 1L;

    /**
     *  自增id
     */
    @ExcelProperty(value = " 自增id")
    @ApiModelProperty(" 自增id")
    private Long id;

    /**
     * 平台
     */
    @ExcelProperty(value = "平台")
    @ApiModelProperty("平台")
    private String platform;

    /**
     * 渠道标识
     */
    @ExcelProperty(value = "渠道标识")
    @ApiModelProperty("渠道标识")
    private String channel;

    /**
     * 广告标识
     */
    @ExcelProperty(value = "广告标识")
    @ApiModelProperty("广告标识")
    private String aid;

    /**
     * 广告名称
     */
    @ExcelProperty(value = "广告名称")
    @ApiModelProperty("广告名称")
    private String aidName;

    /**
     * 游戏名称
     */
    @ExcelProperty(value = "游戏名称")
    @ApiModelProperty("游戏名称")
    private String gameName;

    /**
     * 注册用户数
     */
    @ExcelProperty(value = "注册用户数")
    @ApiModelProperty("注册用户数")
    private Long users;

    /**
     * 付费用户数
     */
    @ExcelProperty(value = "付费用户数")
    @ApiModelProperty("付费用户数")
    private Long payUsers;

    /**
     * 付费总金额
     */
    @ExcelProperty(value = "付费总金额")
    @ApiModelProperty("付费总金额")
    private BigDecimal totalAmount;

    /**
     * 统计日期
     */
    @ExcelProperty(value = "统计日期")
    @ApiModelProperty("统计日期")
    private String createDate;


    /**
     * 广告点击次数
     */
    @ExcelProperty(value = "广告点击次数")
    @ApiModelProperty(value = "广告点击次数", required = true)
    private Long clickCnt;

    /**
     * 激活用户数
     */
    @ExcelProperty(value = "激活用户数")
    @ApiModelProperty(value = "激活用户数", required = true)
    private Long activeUsers;

    /**
     * 父渠道标识
     */
    @ExcelProperty(value = "父渠道标识")
    @ApiModelProperty(value = "父渠道标识", required = true)
    private String preChannel;

    /**
     * 新增创角数
     */
    @ExcelProperty(value = "新增创角数")
    @ApiModelProperty(value = "新增创角数", required = true)
    private Long newRoles;

}
