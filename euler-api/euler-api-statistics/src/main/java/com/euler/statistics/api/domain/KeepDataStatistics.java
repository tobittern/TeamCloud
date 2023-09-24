package com.euler.statistics.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 留存数据统计
 *
 * @author euler
 * @date 2022-04-27
 */
@Data
@TableName("keep_data_statistics")
public class KeepDataStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 数据批次号，yyyyMMddHHmmss
     */
    private Long batchNo;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 主渠道id
     */
    private Integer channelId;

    /**
     * 渠道名称
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
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    private String operationPlatform;

    /**
     * 注册日期
     */
    private Date registTime;

    /**
     * 登录日期
     */
    private Date loginTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
