package com.euler.statistics.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 对象 platform_tongji_datas
 *
 * @author euler
 * @date 2022-09-28
 */
@Data
@TableName("platform_tongji_datas")
public class PlatformTongjiDatas {

private static final long serialVersionUID=1L;

    /**
     * 创建日期
     */
    private String createDate;
    /**
     * 自增主键标识(不显示)
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 广告平台名称
     */
    private String platform;
    /**
     * 分包号
     */
    private String packageCode;
    /**
     * 当日新增用户数
     */
    private Long registerUsers;
    /**
     * 付费次数
     */
    private Long payCnt;
    /**
     * 首日付费次数
     */
    private Long firstPayCnt;
    /**
     * 首日付费用户数
     */
    private Long firstPayUsers;
    /**
     * 首日付费总金额
     */
    private BigDecimal firstRelTotal;
    /**
     * 实际成本
     */
    private BigDecimal relCost;
    /**
     * 首日LTV
     */
    private String ltv;
    /**
     * 首日ROI
     */
    private String roi;

    /**
     * 渠道ID
     */
    private Integer channelId;

}
