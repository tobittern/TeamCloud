package com.euler.community.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.community.domain.bo.DynamicTopicBo;
import com.euler.community.domain.entity.DynamicTopic;
import com.euler.community.mapper.DynamicTopicMapper;
import com.euler.community.service.IDynamicTopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 动态话题Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DynamicTopicServiceImpl extends ServiceImpl<DynamicTopicMapper,DynamicTopic> implements IDynamicTopicService {

    private final DynamicTopicMapper baseMapper;

    /**
     * 动态话题关联数据添加
     */
    @Override
    public int insertDynamicTopic(DynamicTopicBo bo) {
        if (!StringUtils.isNotBlank(bo.getTopicIds())) {
            return 0;
        }
        int rows = 1;
        // 新增关联数据
        if (bo.getDynamicId() == null) {
            return rows;
        }
        Integer[] insertData = Convert.toIntArray(bo.getTopicIds().split(","));
        List<Integer> searchIds = Arrays.asList(insertData);

        List<Integer> newSearchIds = new ArrayList<>();
        // 每个动态最多添加5个话题
        if (searchIds != null && searchIds.size() > 5) {
            newSearchIds = searchIds.subList(0, 5);
        } else {
            newSearchIds = searchIds;
        }
        if (newSearchIds != null && newSearchIds.size() > 0) {
            // 获取到这些添加的话题
            List<DynamicTopic> list = new ArrayList<DynamicTopic>();
            for (var id : newSearchIds) {
                // 过滤掉不能正常使用的话题
                DynamicTopic rm = new DynamicTopic();
                rm.setDynamicId(bo.getDynamicId());
                rm.setTopicId(id.longValue());
                list.add(rm);
            }
            if (list.size() > 0) {
                rows = baseMapper.insertBatch(list) ? list.size() : 0;
            }
        }
        return rows;
    }


    /**
     * 游戏关联数据修改
     */
    @Override
    public int updateDynamicTopic(DynamicTopicBo bo) {
        // 删除相对应的关联数据
        baseMapper.delete(new LambdaQueryWrapper<DynamicTopic>()
            .eq(DynamicTopic::getDynamicId, bo.getDynamicId()));
        // 新增
        return insertDynamicTopic(bo);
    }

}
