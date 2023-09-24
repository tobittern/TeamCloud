package com.euler.platform.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 开放平台游戏转移记录业务对象 open_game_transfer_log
 *
 * @author euler
 * @date 2022-07-25
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("开放平台游戏转移记录业务对象")
public class OpenGameTransferLogDto extends PageQuery {

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    /**
     * 原始拥有的开放平台认证用户id
     */
    @ApiModelProperty(value = "原始拥有的开放平台认证用户id", required = true)
    private Long originalOpid;

    /**
     * 原始拥有者账号名
     */
    @ApiModelProperty(value = "原始拥有者账号名", required = true)
    private String originalUsername;

    /**
     * 原始公司名
     */
    @ApiModelProperty(value = "原始公司名", required = true)
    private String originalCompanyName;

    /**
     * 转移给的开放平台认证用户id
     */
    @ApiModelProperty(value = "转移给的开放平台认证用户id", required = true)
    private Long transferOpid;

    /**
     * 转移给的公司名
     */
    @ApiModelProperty(value = "转移给的公司名", required = true)
    private String transferCompanyName;

    /**
     * 转移之后的账号名
     */
    @ApiModelProperty(value = "转移之后的账号名", required = true)
    private String transferUsername;


}
