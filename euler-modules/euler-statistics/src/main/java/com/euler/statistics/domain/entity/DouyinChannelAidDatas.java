package com.euler.statistics.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对象 douyin_channel_aid_datas
 * @author euler
 * @date 2022-07-13
 */
@Data
@TableName("douyin_channel_aid_datas")
public class DouyinChannelAidDatas {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 平台
     */
    private String platform;
    /**
     * 渠道标识
     */
    private String channel;
    /**
     * 广告标识
     */
    private String aid;
    /**
     * 广告名称
     */
    private String aidName;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 注册用户数
     */
    private Long users;
    /**
     * 付费用户数
     */
    private Long payUsers;
    /**
     * 付费总金额
     */
    private BigDecimal totalAmount;
    /**
     * 统计日期
     */
    private String createDate;
    /**
     * 广告点击次数
     */
    private Long clickCnt;
    /**
     * 激活用户数
     */
    private Long activeUsers;

    /**
     * 父渠道标识
     */
    private String preChannel;
    /**
     * 新增创角数
     */
    private Long newRoles;

}
