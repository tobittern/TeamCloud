package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广告成本管理对象 advertising_cost
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("advertising_cost")
public class AdvertisingCost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 广告平台
     */
    private String advertisingPlatform;
    /**
     * 广告成本日期
     */
    private String costDate;
    /**
     * 成本，精确到小数点后两位
     */
    private Float cost;
    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;


    /**
     * 游戏id
     */
    private Integer gameId;


    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏运行平台 (1 安卓  2 ios  3 h5)
     */
    private Integer gameOperationPlatform;

    /**
     * 媒体id
     */
    private Integer mediaId;


    /**
     * 媒体名称
     */
    private String mediaName;

}
