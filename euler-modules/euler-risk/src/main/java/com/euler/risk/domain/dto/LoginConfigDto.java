package com.euler.risk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author euler
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("登录配置对象")
public class LoginConfigDto extends PageQuery {
    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 平台标识（1:sdk 4:九区玩家app）
     */
    @ApiModelProperty(value = "平台标识（1:sdk 4:九区玩家app）")
    private String platformType;

    /**
     * 是否是全局配置（0:不是 1:是）
     */
    @ApiModelProperty(value = "是否是全局配置（0:不是 1:是）")
    private String globalConfig;

    /**
     * 平台（1:Android 2:ios 3:h5）
     */
    @ApiModelProperty(value = "平台（1:Android 2:ios 3:h5）")
    private String platform;

}
