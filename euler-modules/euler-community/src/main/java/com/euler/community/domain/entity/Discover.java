package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 发现配置对象 discover
 *
 * @author euler
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("discover")
public class Discover extends BaseEntity {

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
     * 模块标题
     */
    private String title;
    /**
     * 模块介绍
     */
    private String introduce;
    /**
     * 模块类型, 1纵向编组，2横向编组，3banner编组
     */
    private String moduleType;
    /**
     * 状态，0待启用，1已启用，2已停用
     */
    private String status;
    /**
     * 应用系统，1android，2ios,3h5
     */
    private String applicationSystem;
    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    private Integer level;
    /**
     * 具体内容，格式为json格式
     * 类型是1和2的时候，[{"gameId":"101","tag":"热门游戏1"},{"gameId":"102","tag":"热门游戏2"}]
     * 类型是3的时候，[{"bannerGroupId":"201"}]
     */
    private String content;
    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
