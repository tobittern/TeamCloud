package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 对象 sign_config
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sign_config")
public class SignConfig extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Integer id;
    /**
     * 星期几
     */
    private Integer week;
    /**
     * 分数
     */
    private Integer score;

}
