package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 弹窗管理对象 popup
 *
 * @author euler
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("popup")
public class Popup extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 专题名称
     */
    private String title;

    /**
     * 每天显示次数
     */
    private Integer times;

    /**
     * 弹窗显示开始时间
     */
    private Date startTime;

    /**
     * 弹窗显示结束时间
     */
    private Date endTime;

    /**
     * 弹窗图片
     */
    private String popupIcon;

    /**
     * 跳转url
     */
    private String jumpUrl;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    private String status;

    /**
     * 弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单
     */
    private String position;

    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
