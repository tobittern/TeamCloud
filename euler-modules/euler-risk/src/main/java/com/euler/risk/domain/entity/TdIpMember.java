package com.euler.risk.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * ip账号信息对象 td_ip_member
 *
 * @author euler
 * @date 2022-08-24
 */
@Data
@TableName("td_ip_member")
public class TdIpMember {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 设备id，最后登录设备id
     */
    private String deviceId;
    /**
     * ip区域地址
     */
    private String ipAddress;
    /**
     * 账号
     */
    private String account;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户id，不登录为0
     */
    private Long userId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
