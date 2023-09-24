package com.euler.statistics.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对象 platform_datas
 * @author euler
 * @date 2022-07-13
 */
@Data
@TableName("platform_datas")
public class PlatformDatas {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 平台
     */
    private String platform;
    /**
     * 本地后台注册用户数
     */
    private Long bdUsers;
    /**
     * 广告平台注册用户数
     */
    private Long ptUsers;
    /**
     * 本地后台付费用户数
     */
    private Long bdPayUsers;
    /**
     * 广告平台付费用户数
     */
    private Long ptPayUsers;
    /**
     * 本地后台付费总金额
     */
    private BigDecimal bdTotalAmount;
    /**
     * 广告平台付费总金额
     */
    private BigDecimal ptTotalAmount;
    /**
     * 统计日期
     */
    private String createDate;

    /**
     * 广告平台新增创角数
     */
    private Long ptNewRoles;

    /**
     * 本地后台新增创角数
     */
    private Long bdNewRoles;


}
