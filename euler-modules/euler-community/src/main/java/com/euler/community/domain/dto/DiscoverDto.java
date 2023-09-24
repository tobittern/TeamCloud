package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发现配置业务对象 discover
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@ApiModel("发现配置业务对象")
public class DiscoverDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 模块标题
     */
    @ApiModelProperty(value = "模块标题")
    private String title;

    /**
     * 模块介绍
     */
    @ApiModelProperty(value = "模块介绍")
    private String introduce;

    /**
     * 模块类型, 1纵向编组，2横向编组，3banner编组
     */
    @ApiModelProperty(value = "模块类型, 1纵向编组，2横向编组，3banner编组")
    private String moduleType;

    /**
     * 状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 应用系统，1android，2ios,3h5
     */
    @ApiModelProperty(value = "应用系统，1android，2ios,3h5")
    private String applicationSystem;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ApiModelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高")
    private Integer level;
}
