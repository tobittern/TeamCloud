package com.euler.community.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author euler
 * @date 2022-04-06
 */
@Data
@ApiModel("sdk用户的基础信息")
@ExcelIgnoreUnannotated
public class MemberVo {

    private static final long serialVersionUID = 1L;

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 用户性别（1男 0女 2未知）
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
     * 是否是官方账号
     */
    private Integer isOfficial = 0;
}
