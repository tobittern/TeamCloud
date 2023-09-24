package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 屏蔽信息对象 shield
 *
 * @author euler
 * @date 2022-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shield")
public class Shield extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * id
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 业务类型，1：用户：2动态
     */
    private Integer businessType;
    /**
     * 原因
     */
    private String reason;
    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
