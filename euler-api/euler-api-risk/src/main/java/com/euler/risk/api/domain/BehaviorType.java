package com.euler.risk.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 行为类型对象 behavior_type
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("behavior_type")
public class BehaviorType extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 行为类型id
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 平台，header数据，1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    private String platform;
    /**
     * 设备，header数据，0：PC，1：安卓，2：ios，3：h5，4：小程序
     */
    private String device;

    /**
     * 拦截行为类型，0：请求接口拦截，1：代码内拦截
     */
    private  String type;
    /**
     * 模块
     */
    private String model;
    /**
     * 行为类型标识
     */
    private String code;
    /**
     * 行为请求url
     */
    private String path;
    /**
     * 行为类型名称
     */
    private String name;
    /**
     * 行为类型描述
     */
    private String description;

    /**
     * 处理数据反射类表达式
     */
    private String reflectExpression;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
     @TableLogic
    private String delFlag;

}
