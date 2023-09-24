package com.euler.sdk.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户信息对象 member
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member")
public class Member extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
     @TableId(value = "id")
    private Long id;
    /**
     * 全网唯一标识
     */
    private String uniqueId;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
     @TableLogic
    private String delFlag;

    /**
     * 注册平台，1：sdk，2：开放平台，3：管理后台 4：APP，5：小程序
     */
    private Integer platform;

}
