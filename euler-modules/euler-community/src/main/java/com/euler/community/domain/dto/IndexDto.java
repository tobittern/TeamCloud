package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 动态收藏业务对象 collect
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("首页动态展示")
public class IndexDto extends PageQuery {


    /**
     * 搜索关键词
     */
    @ApiModelProperty(value = "搜索关键词")
    private String keyword;

    /**
     * 搜索类型
     */
    @ApiModelProperty(value = "搜索类型")
    private Integer type = 1;

    /**
     * 搜索位置 1 主页  2 关注列表 3 个人主页 4 收藏列表 5点赞列表
     */
    @ApiModelProperty(value = "搜索位置")
    private Integer position = 1;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long memberId;

    /**
     * 排除的动态ID 多个用逗号隔开
     */
    @ApiModelProperty(value = "排除的动态ID")
    private String excludeDynamicIdString;

    /**
     * 查询一批用户
     */
    @ApiModelProperty(value = "用户ID")
    private List<Long> searchMemberIds;

    /**
     * 查询一批动态
     */
    @ApiModelProperty(value = "查询的动态ID")
    private List<Long> searchDynamicIds;


    /**
     * 排除的动态ID
     */
    @ApiModelProperty(value = "排除的动态ID")
    private List<Long> excludeDynamicIds;

}
