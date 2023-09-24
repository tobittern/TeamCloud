package com.euler.community.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.community.domain.bo.DynamicTopicBo;
import com.euler.community.domain.entity.DynamicTopic;

/**
 * 活动Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IDynamicTopicService extends IService<DynamicTopic> {

    /**
     * 动态关联话题添加
     */
    int insertDynamicTopic(DynamicTopicBo bo);

    /**
     * 动态关联话题修改
     */
    int updateDynamicTopic(DynamicTopicBo bo);
}
