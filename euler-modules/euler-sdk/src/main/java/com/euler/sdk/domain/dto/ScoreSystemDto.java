package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 积分体系业务对象 score_system
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("积分体系业务对象")
public class ScoreSystemDto extends PageQuery {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 游戏用户id
     */
    @ApiModelProperty(value = "游戏用户id")
    private Long userId;

    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Long score;

    /**
     * 类型 0:签到积分 1:首次注册积分
     */
    @ApiModelProperty(value = "类型 0:签到积分 1:首次注册积分")
    private String type;
}
