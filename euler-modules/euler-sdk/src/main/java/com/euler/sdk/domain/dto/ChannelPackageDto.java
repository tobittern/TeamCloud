package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 渠道分组业务对象 channel_group
 *
 * @author euler
 * @date 2022-04-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("渠道分组业务对象")
public class ChannelPackageDto extends PageQuery {

    /**
     * 主渠道ID
     */
    @ApiModelProperty(value = "主渠道ID", required = true)
    private Integer channelId;

    /**
     * 对应游戏ID
     */
    @ApiModelProperty(value = "对应游戏ID", required = true)
    private Integer gameId;


    /**
     * 新游戏名
     */
    @ApiModelProperty(value = "新游戏名", required = true)
    private String newGameName;

    /**
     * 游戏版本
     */
    @ApiModelProperty(value = "游戏版本", required = true)
    private String version;

    /**
     * 分包的标签
     */
    @ApiModelProperty(value = "分包的标签", required = true)
    private String label;

    /**
     * 分包状态
     */
    @ApiModelProperty(value = "分包状态", required = true)
    private Integer status;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;
}
