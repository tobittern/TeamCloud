package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据统计 - 每日的ltv基础数据统计对象 statistics_everyday_ltv_basedata
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@TableName("basic_statistics")
public class BasicStatisticsVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 区服id
     */
    private String serverId;
    /**
     * 加密
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

}
