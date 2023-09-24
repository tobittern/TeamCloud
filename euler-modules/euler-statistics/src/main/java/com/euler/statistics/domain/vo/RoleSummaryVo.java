package com.euler.statistics.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 游戏用户管理视图对象 game_user_management
 *
 * @author euler
 * @date 2022-04-02
 */
@Data
@ApiModel("游戏用户管理视图对象")
@ExcelIgnoreUnannotated
public class RoleSummaryVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * role_id
     */
    private String roleId;

    /**
     * server_id
     */
    private String serverId;


    /**
     * gameId
     */
    private Integer gameId;


    /**
     * packageCode
     */
    private String packageCode;

    /**
     * createTime
     */
    private Date createTime;

    public RoleSummaryVo(String roleId, String serverId, Integer gameId, String packageCode, Date createTime) {
        this.setRoleId(roleId);
        this.setServerId(serverId);
        this.setGameId(gameId);
        this.setPackageCode(packageCode);
        this.setCreateTime(createTime);
    }
}
