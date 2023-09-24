package com.euler.risk.domain.dto;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 封号列业务对象 banlist
 *
 * @author euler
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("封号列业务对象")
public class BanlistDto extends PageQuery {

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键", required = true)
    private Integer id;

    /**
     * 平台1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    @ApiModelProperty(value = "平台", required = true)
    private String platform;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    private Long memberId;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true)
    private String account;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    private String nickName;

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 封号开始时间
     */
    @ApiModelProperty(value = "封号开始时间", required = true)
    private String startTime;

    /**
     * 封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁
     */
    @ApiModelProperty(value = "封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁", required = true)
    private String endTime;

    /**
     * 封号类型
     */
    @ApiModelProperty(value = "封号类型", required = true)
    private String banType;

    /**
     * 封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备
     */
    @ApiModelProperty(value = "封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备", required = true)
    private Integer searchType;


}
