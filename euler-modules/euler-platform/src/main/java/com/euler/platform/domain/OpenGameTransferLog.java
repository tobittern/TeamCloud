package com.euler.platform.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 开放平台游戏转移记录对象 open_game_transfer_log
 *
 * @author euler
 * @date 2022-07-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("open_game_transfer_log")
public class OpenGameTransferLog extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 游戏ID
     */
    private Integer gameId;
    /**
     * 原始拥有的开放平台认证用户id
     */
    private Long originalOpid;
    /**
     * 原始拥有者账号名
     */
    private String originalUsername;
    /**
     * 原始公司名
     */
    private String originalCompanyName;
    /**
     * 转移给的开放平台认证用户id
     */
    private Long transferOpid;
    /**
     * 转移给的公司名
     */
    private String transferCompanyName;
    /**
     * 转移之后的账号名
     */
    private String transferUsername;
    /**
     * 删除状态 0正常 2删除
     */
     @TableLogic
    private String delFlag;

}
