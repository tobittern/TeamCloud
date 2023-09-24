package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 主渠道视图对象 channel
 *
 * @author euler
 * @date 2022-04-01
 */
@Data
@ApiModel("主渠道视图对象")
@ExcelIgnoreUnannotated
public class ChannelVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * userId
     */
    @ExcelProperty(value = "userId")
    @ApiModelProperty("userId")
    private Long userId;

    /**
     * 渠道名
     */
    @ExcelProperty(value = "渠道名")
    @ApiModelProperty("渠道名")
    private String channelName;

    /**
     * 渠道主账号名称
     */
    @ExcelProperty(value = "渠道主账号名称")
    @ApiModelProperty("渠道主账号名称")
    private String adminName;

    /**
     * 渠道游戏数量
     */
    @ExcelProperty(value = "渠道游戏数量")
    @ApiModelProperty("渠道游戏数量")
    private Integer gameNum;

    /**
     * 渠道状态 0正常 1停用
     */
    @ExcelProperty(value = "渠道状态 0正常 1停用")
    @ApiModelProperty("渠道状态 0正常 1停用")
    private Integer status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

}
