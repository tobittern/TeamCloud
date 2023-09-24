package com.euler.common.core.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("渠道关联游戏搜索")
public class SelectGameDto implements Serializable {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * type
     */
    @ApiModelProperty(value = "type")
    private Integer type;

    /**
     * gameName
     */
    @ApiModelProperty(value = "gameName")
    private String gameName;

    /**
     * operationPlatform
     */
    @ApiModelProperty(value = "operationPlatform")
    private Integer operationPlatform;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    private Integer pageSize;

    /**
     * 当前页数
     */
    @ApiModelProperty("当前页数")
    private Integer pageNum;

    /**
     * 排序列
     */
    @ApiModelProperty("排序列")
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @ApiModelProperty(value = "排序的方向", example = "asc,desc")
    private String isAsc;


}
