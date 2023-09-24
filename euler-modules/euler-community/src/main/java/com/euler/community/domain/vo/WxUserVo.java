package com.euler.community.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
public class WxUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 动态数量
     */
    @ApiModelProperty("动态数量")
    private Integer dynamicCount = 0;

    /**
     * 评论数量
     */
    @ApiModelProperty("评论数量")
    private Integer commentCount = 0;

    /**
     * 收藏数量
     */
    @ApiModelProperty("收藏数量")
    private Integer collectCount = 0;

    /**
     * 点赞数量
     */
    @ApiModelProperty("点赞数量")
    private Integer praiseCount = 0;

}
