package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * banner列对象 banner
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("banner")
public class Banner extends BaseEntity {

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
     * banner名
     */
    private String bannerName;
    /**
     * banner图
     */
    private String bannerIcon;
    /**
     * 跳转路径
     */
    private String jumpUrl;
    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
