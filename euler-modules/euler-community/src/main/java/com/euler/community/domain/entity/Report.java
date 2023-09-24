package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 举报对象 report
 *
 * @author euler
 * @date 2022-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("report")
public class Report extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 关联的ID
     */
    private Long relationId;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 举报类型 1动态 2评论
     */
    private String type;

    /**
     * 举报理由
     */
    private String reason;

    /**
     * 所属动态的id
     */
    private Long dynamicId;

    /**
     * 是否删除，0未删除，2已删除
     */
    @TableLogic
    private String delFlag;

}
