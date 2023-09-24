package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 开放平台充值金额统计对象 charge_statistics
 *
 * @author euler
 *  2022-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("charge_statistics")
public class ChargeStatistics extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 游戏服务器id
     */
    private String serverId;
    /**
     * 游戏服务器名称
     */
    private String serverName;
    /**
     * 新增充值
     */
    private BigDecimal newIncreaseCharge;
    /**
     * 新注册充值
     */
    private BigDecimal newRegisterCharge;
    /**
     * 新注册平均充值
     */
    private BigDecimal newRegisterAvgCharge;
    /**
     * 总充值数据
     */
    private BigDecimal totalCharge;
    /**
     * 统计日期
     */
    private Date day;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
