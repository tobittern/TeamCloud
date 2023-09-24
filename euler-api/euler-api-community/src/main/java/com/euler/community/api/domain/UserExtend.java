package com.euler.community.api.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户扩展表 user_extend
 *
 * @author euler
 * @date 2022-07-07
 */
@Data
@TableName("user_extend")
public class UserExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "member_id")
    private Long memberId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 签到时间
     */
    private Date signDate;

    /**
     * 是否是官方账号  0不是  1是
     */
    private Integer isOfficial;

    /**
     * 性别 1男 0女 2未知
     */
    private String sex;

    private  Date updateTime;

}
