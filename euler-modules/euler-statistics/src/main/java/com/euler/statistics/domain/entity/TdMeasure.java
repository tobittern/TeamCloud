package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.euler.common.core.web.domain.BaseEntity;

/**
 * 指标维对象 td_measure
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("td_measure")
public class TdMeasure extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 模块code
     */
    private String modelId;
    /**
     * 模块
     */
    private String modelName;
    /**
     * 指标标识
     */
    private String measureId;
    /**
     * 指标名称
     */
    private String measureName;
    /**
     * 指标描述
     */
    private String measureDesc;
    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

}
