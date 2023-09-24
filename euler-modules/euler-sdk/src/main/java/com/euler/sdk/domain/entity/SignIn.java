package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * 对象 sign_in
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sign_in")
public class SignIn extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 签到用户
     */
    private Long memberId;
    /**
     * 签到活动ID
     */
    private Integer activeId;
    /**
     * 签到日期
     */
    private Integer week;
    /**
     * 积分数
     */
    private Integer score;

    /**
     * 是否删除
     */
     @TableLogic
    private String delFlag;

}
