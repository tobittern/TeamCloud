package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户更换手机号记录对象 member_change_phone_record
 *
 * @author euler
 * @date 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_change_phone_record")
public class MemberChangePhoneRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 原始手机号码
     */
    private String originalMobile;
    /**
     * 新手机号
     */
    private String newMobile;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
