package com.euler.sdk.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 会员详细信息对象 member_profile
 *
 * @author euler
 * @date 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_profile")
public class MemberProfile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 会员详情id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 会员id
     */
    private Long memberId;
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
     * 实名认证状态，0：未认证，1：已认证
     */
    private String verifyStatus;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 个人简介
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 注册ip地址
     */
    private String registerIp;
    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录设备Id
     */
    private String loginDeviceId;
    /**
     * 最后登录时间
     */
    private Date loginDate;
    /**
     * 签到时间
     */
    private Date signDate;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;
    /**
     * 来源渠道id
     */
    private Integer channelId;
    /**
     * 来源渠道名称
     */
    private String channelName;
    /**
     * 渠道号，分包号
     */
    private String packageCode;

    /**
     * 来源游戏id
     */
    private Integer gameId;

    /**
     * 来源游戏名称
     */
    private String gameName;
    /**
     * 会话次数
     */
    private Integer sessionNum;

    /**
     * 是否是官方账号 0不是  1是
     */
    private Integer isOfficial;

    /**
     * 省份id
     */
    private Integer provinceId;
    /**
     * 省份名称
     */
    private String province;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 区县id
     */
    private Integer areaId;
    /**
     * 区县名称
     */
    private String area;

}
