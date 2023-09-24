package com.euler.platform.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【游戏管理】业务对象 open_game
 *
 * @author open
 * @date 2022-02-18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("【游戏管理】业务对象")
public class OpenGamePageDto extends PageQuery {

    /**
     * 主键自增
     */
    @ApiModelProperty(value = "主键自增")
    private Integer id;

    /**
     * 查询ids
     */
    @ApiModelProperty(value = "查询ids")
    private String ids;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 对应版本的ID
     */
    @ApiModelProperty("对应版本的ID")
    private Integer versionId;

    /**
     * 游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)
     */
    @ApiModelProperty(value = "游戏状态(0 初始状态 1审核中  2审核成功  3审核失败 4归档)")
    private Integer gameStatus;

    /**
     * 游戏上架状态(0待上线  1上线  2下线)
     */
    @ApiModelProperty(value = "游戏上架状态(0待上线  1上线  2下线)")
    private Integer onlineStatus;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 游戏类目
     */
    @ApiModelProperty(value = "游戏类目")
    private String gameCategory;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓  2 ios  3 h5)")
    private Integer operationPlatform;

    /**
     * 付费类型(1 有付费 2无付费)
     */
    @ApiModelProperty(value = "付费类型(1 有付费 2无付费)")
    private Integer payType;


}
