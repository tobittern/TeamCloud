package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 封号列对象 banlist
 *
 * @author euler
 * @date 2022-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("banlist")
public class Banlist extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 平台1：sdk，2：开放平台，3：管理后台 4：九区玩家APP
     */
    private String platform;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 账号
     */
    private String account;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 游戏ID
     */
    private Integer gameId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 封号开始时间
     */
    private Date startTime;
    /**
     * 封号截止时间 时间等于2194-03-05 00:00:00 默认为永久封禁
     */
    private Date endTime;
    /**
     * 封号类型
     */
    private String banType;
    /**
     * 封号原因
     */
    private String reason;
    /**
     * 封号查询类型  1手机号 2用户id 3 ip 4 身份证 5设备
     */
    private Integer searchType;
    /**
     * 搜索的key
     */
    private String searchKey;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
