package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 游戏用户管理对象
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
public class GameUserManagementDto extends PageQuery {

    @ApiModelProperty(value = "用户id")
    private Long memberId;

    @ApiModelProperty(value = "角色昵称")
    private String roleName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty("来源渠道号")
    private  String packageCode;

    @ApiModelProperty("来源游戏")
    private  String gameName;

    @ApiModelProperty("游戏vip等级")
    private String vipLevel;

    @ApiModelProperty("最后登录时间")
    private String lastLoginTime;

    @ApiModelProperty(value = "主渠道id")
    private Integer channelId;

    @ApiModelProperty(value = "角色id")
    private String roleId;

}
