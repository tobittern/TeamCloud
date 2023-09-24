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
import java.io.Serializable;


/**
 * 视图对象 platform_tongji_channel_aid_datas
 *
 * @author euler
 * @date 2022-09-28
 */
@Data
@ApiModel("视图对象")
@ExcelIgnoreUnannotated
public class PlatformTongjiChannelAidDatasVo  implements Serializable  {

    private static final long serialVersionUID = 1L;


    /**
     * 自增主键标识(不显示)
     */
    @ExcelProperty(value = "自增主键标识(不显示)")
    @ApiModelProperty("自增主键标识(不显示)")
    private Long id;

    /**
     * 平台
     */
    @ExcelProperty(value = "平台")
    @ApiModelProperty("平台")
    private String platform;

    /**
     * 父渠道标识
     */
    @ExcelProperty(value = "父渠道标识")
    @ApiModelProperty("父渠道标识")
    private String preChannel;

    /**
     * 渠道标识
     */
    @ExcelProperty(value = "渠道标识")
    @ApiModelProperty("渠道标识")
    private String channel;

    /**
     * 统计日期
     */
    @ExcelProperty(value = "统计日期")
    @ApiModelProperty("统计日期")
    private String createDate;

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
     * 广告点击次数
     */
    @ExcelProperty(value = "广告点击次数")
    @ApiModelProperty("广告点击次数")
    private Long clickCnt;

    /**
     * 激活用户数
     */
    @ExcelProperty(value = "激活用户数")
    @ApiModelProperty("激活用户数")
    private Long activeUsers;

    /**
     * 游戏名
     */
    @ExcelProperty(value = "游戏名")
    @ApiModelProperty("游戏名")
    private String gameName;

    /**
     * 注册用户数
     */
    @ExcelProperty(value = "注册用户数")
    @ApiModelProperty("注册用户数")
    private Long registUsers;

    /**
     * 付费用户数
     */
    @ExcelProperty(value = "付费用户数")
    @ApiModelProperty("付费用户数")
    private Long payUsers;

    /**
     * 付费金额
     */
    @ExcelProperty(value = "付费金额")
    @ApiModelProperty("付费金额")
    private BigDecimal totalAmount;

    /**
     * 渠道ID
     */
    @ExcelProperty(value = "渠道ID")
    @ApiModelProperty(value = "渠道ID")
    private Integer channelId;


}
