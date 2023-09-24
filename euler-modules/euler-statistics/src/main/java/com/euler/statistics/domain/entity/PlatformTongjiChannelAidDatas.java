package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 对象 platform_tongji_channel_aid_datas
 *
 * @author euler
 * @date 2022-09-28
 */
@Data
@TableName("platform_tongji_channel_aid_datas")
public class PlatformTongjiChannelAidDatas {

    private static final long serialVersionUID = 1L;
    /**
     * 自增主键标识(不显示)
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 平台
     */
    private String platform;
    /**
     * 父渠道标识
     */
    private String preChannel;
    /**
     * 渠道标识
     */
    private String channel;
    /**
     * 统计日期
     */
    private String createDate;
    /**
     * 广告标识
     */
    private String aid;
    /**
     * 广告名称
     */
    private String aidName;
    /**
     * 广告点击次数
     */
    private Long clickCnt;
    /**
     * 激活用户数
     */
    private Long activeUsers;
    /**
     * 游戏名
     */
    private String gameName;
    /**
     * 注册用户数
     */
    private Long registUsers;
    /**
     * 付费用户数
     */
    private Long payUsers;
    /**
     * 付费金额
     */
    private BigDecimal totalAmount;

    /**
     * 渠道ID
     */
    private Integer channelId;

}
