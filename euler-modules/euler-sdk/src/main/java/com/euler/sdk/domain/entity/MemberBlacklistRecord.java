package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 黑名单封禁操作记录对象 member_blacklist_record
 *
 * @author euler
 * @date 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_blacklist_record")
public class MemberBlacklistRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 描述
     */
    private String description;
    /**
     * 封号截止时间
     */
    private String endTime;
    /**
     * 封号原因
     */
    private String record;
    /**
     * 操作类型 1封号 2解封
     */
    private Integer type;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
