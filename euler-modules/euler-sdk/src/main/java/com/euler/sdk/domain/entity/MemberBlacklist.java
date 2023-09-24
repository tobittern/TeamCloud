package com.euler.sdk.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户黑名单列对象 member_blacklist
 *
 * @author euler
 * @date 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_blacklist")
public class MemberBlacklist extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 平台 0全平台 1sdk  2app
     */
    private Integer platform;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 游戏ID
     */
    private Integer gameId;
    /**
     * 封号截止时间
     */
    private Date endTime;
    /**
     * 封号原因
     */
    private String record;
    /**
     * 是否是永久封号  0 不是  1是
     */
    private Integer isPermanent;
    /**
     * ip地址
     */
    private String ip;

    /**
     * 类型 1用户 2IP
     */
    private Integer type;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @TableLogic
    private String delFlag;

}
