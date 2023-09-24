package com.euler.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SDK子菜单对象 sdk_sub_menu
 *
 * @author euler
 * @date 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sdk_sub_menu")
public class SdkSubMenu extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * sdk子菜单id
     */
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * sdk主菜单id
     */
    private Integer mainMenuId;

    /**
     * 选中时的图标
     */
    private String icon;

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
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
