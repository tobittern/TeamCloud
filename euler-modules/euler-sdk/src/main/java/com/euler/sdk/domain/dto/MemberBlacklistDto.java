package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户黑名单列业务对象 member_blacklist
 *
 * @author euler
 * @date 2022-06-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户黑名单列业务对象")
public class MemberBlacklistDto extends PageQuery {

    /**
     * 平台 0全平台 1sdk  2app
     */
    @ApiModelProperty(value = "平台 0全平台 1sdk  2app", required = true)
    private Integer platform;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long memberId;

    /**
     * 用户IDS
     */
    @ApiModelProperty(value = "用户IDS", required = true)
    private List<Long> memberIds;

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    /**
     * 是否是永久封号  0 不是  1是
     */
    @ApiModelProperty(value = "是否是永久封号  0 不是  1是", required = true)
    private Integer isPermanent;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ip;

    /**
     * 类型 1用户 2IP
     */
    @ApiModelProperty("类型 1用户 2IP")
    private Integer type;


}
