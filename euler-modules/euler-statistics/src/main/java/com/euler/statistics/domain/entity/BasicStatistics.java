package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 数据统计 - 每日的ltv基础数据统计对象 statistics_everyday_ltv_basedata
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("basic_statistics")
public class BasicStatistics extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * selectKey
     */
    private String selectKey;
    /**
     * 关联game_user_management的id
     */
    private Integer gumId;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 服务器id
     */
    private String serverId;
    /**
     * 角色ID+区服ID md5加密的值
     */
    private String roleServerMd5;
    /**
     * 主渠道id
     */
    private Integer channelId;
    /**
     * 主渠道名
     */
    private String channelName;
    /**
     * 渠道code
     */
    private String packageCode;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名
     */
    private String gameName;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    private Integer operationPlatform;
    /**
     * 日期标签
     */
    private String dateLabel;
    /**
     * 角色上报时间
     */
    private Date loginTime;
    /**
     * 用户注册时间
     */
    private Date registerTime;
    /**
     * 删除(0正常 2删除)
     */
    @TableLogic
    private String delFlag;

}
