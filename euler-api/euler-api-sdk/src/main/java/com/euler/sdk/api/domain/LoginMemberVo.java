package com.euler.sdk.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginMemberVo implements Serializable {
    /**
     * 用户ID
     */
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
     * 实名认证状态
     */
    private String verifyStatus;

    /**
     * 身份证
     */
    private String idCardNo;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 来源渠道名称
     */
    private String channelName;
    /**
     * 渠道号，分包号
     */
    private String packageCode;
    /**
     * 来源游戏名称
     */
    private String gameName;

    /**
     * 头像
     */
    private  String avatar;

    /**
     * 用户性别（1男 0女 2未知）
     */
    private  String sex;

    /**
     * 最后登录时间
     */
    private Date loginDate;


}
