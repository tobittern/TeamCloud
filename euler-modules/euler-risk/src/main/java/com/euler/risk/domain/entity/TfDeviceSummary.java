package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 设备数据汇总对象 tf_device_summary
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@TableName("tf_device_summary")
public class TfDeviceSummary {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 汇总日期
     */
    private Date dateId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 注册次数
     */
    private Integer registerNum;
    /**
     * 登录次数
     */
    private Integer loginNum;
    /**
     * 角色创建次数
     */
    private Integer roleCreateNum;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
