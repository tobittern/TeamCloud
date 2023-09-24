package com.euler.sdk.api.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 注销会员记录
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
@TableName("cancellation_log")
public class CancellationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 注销原因
     */
    private String reason;

    /**
     * 注销执行状态，0：未注销，1：注销成功，2：注销失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 会员信息
     */
    private String memberInfo;


    /**
     * 执行次数
     */
    private Integer opNums = 0;

}
