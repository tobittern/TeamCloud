package com.euler.community.domain.bo;

import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务对象 hot_game
 *
 * @author euler
 * @date 2022-06-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class HotGameBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long memberId;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    private Long gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称", required = true)
    private String gameName;

    /**
     * 游戏图片
     */
    @ApiModelProperty(value = "游戏图片", required = true)
    private String gamePic;

    /**
     * 查看游戏次数
     */
    @ApiModelProperty(value = "查看游戏次数", required = true)
    private Integer num;

    /**
     * 平台
     */
    @ApiModelProperty(value = "平台", required = true)
    private Integer operationPlatform;

}
