package com.euler.community.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 动态关联话题对象 dynamic_topic
 *
 * @author euler
 * @date 2022-06-01
 */
@Data
@TableName("dynamic_topic")
public class DynamicTopic {

    private static final long serialVersionUID = 1L;

    /**
     * 动态表主键id
     */
    @TableId(value = "dynamic_id")
    private Long dynamicId;
    /**
     * 转发用户id
     */
    private Long topicId;

}
