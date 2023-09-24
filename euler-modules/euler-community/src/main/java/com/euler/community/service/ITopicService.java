package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.community.domain.dto.TopicDto;
import com.euler.community.domain.entity.Topic;
import com.euler.community.domain.vo.TopicVo;
import com.euler.community.domain.bo.TopicBo;
import com.euler.common.mybatis.core.page.TableDataInfo;

/**
 * 话题Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface ITopicService extends IService<Topic> {

    /**
     * 查询话题
     *
     * @param id 话题主键
     * @return 话题
     */
    TopicVo queryById(Long id);

    /**
     * 查询话题列表
     *
     * @param dto 话题
     * @return 话题集合
     */
    TableDataInfo<TopicVo> queryPageList(TopicDto dto);

    /**
     * 新增话题
     *
     * @param bo 话题
     * @return 结果
     */
    R insertByBo(TopicBo bo);

    /**
     * 修改话题
     *
     * @param bo 话题
     * @return 结果
     */
    R updateByBo(TopicBo bo);

    /**
     * 查询热门话题列表
     *
     * @param dto 话题
     * @return 话题集合
     */
    TableDataInfo<TopicVo> queryList(TopicDto dto);

}
