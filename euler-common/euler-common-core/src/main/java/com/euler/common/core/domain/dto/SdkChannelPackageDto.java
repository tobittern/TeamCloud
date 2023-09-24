package com.euler.common.core.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SdkChannelPackageDto  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主渠道ID
     */
    private Integer channelId=0;

    /**
     * 渠道名称
     */
    private  String channelName;
    /**
     * 对应游戏ID
     */
    private Integer gameId=0;

    /**
     * 游戏名称
     */
    private  String gameName;


    /**
     * 游戏区服
     */
    private String gameServerId;
    /**
     * 游戏区服
     */
    private String gameServerName;

    /**
     * 游戏角色
     */
    private String gameRoleId;

    /**
     * 游戏角色
     */
    private String gameRoleName;


    /**
     * 分包的名称
     */
    private String packageCode;

    /**
     * 版本ID
     */
    private Integer versionId;

    /**
     * 版本
     */
    private String version;








}
