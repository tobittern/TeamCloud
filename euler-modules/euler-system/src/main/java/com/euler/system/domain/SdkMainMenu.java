package com.euler.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SDK菜单对象 sdk_main_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sdk_main_menu")
public class SdkMainMenu extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * sdk菜单ID
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 选中时的图标
     */
    private String icon;

    /**
     * 未选中时的图标
     */
    private String wicon;

    /**
     * 路径
     */
    private String path;

    /**
     * 排序
     */
    private String sort;

    /**
     * 代码
     */
    private String code;

    /**
     * 小圆点
     */
    private String badge;

    /**
     * 是否上下架（1上架 2下架）
     */
    private String isUp;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
