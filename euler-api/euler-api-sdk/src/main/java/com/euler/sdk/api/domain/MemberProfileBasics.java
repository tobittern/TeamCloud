package com.euler.sdk.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员详细信息对象 member_profile
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
public class MemberProfileBasics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 账号
     */
    private String account;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证号
     */
    private String idCardNo;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 设备类型
     */
    private String deviceId;
    /**
     * 设备类型
     */
    private String mobileType;
    /**
     * 最后一次登录时间
     */
    private Date createTime;

}
