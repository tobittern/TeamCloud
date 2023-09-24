package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 搜索历史对象 history_search
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("history_search")
public class HistorySearch extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
     @TableId(value = "id")
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 搜索内容
     */
    private String key;

    /**
     * 对于key的搜索次数
     */
    private Integer num;

    /**
     * 搜索位置(0首页搜索,1发现页搜索)
     */
    private Integer position;

    /**
     * 是否删除，0未删除，2已删除
     */
     @TableLogic
    private String delFlag;

}
