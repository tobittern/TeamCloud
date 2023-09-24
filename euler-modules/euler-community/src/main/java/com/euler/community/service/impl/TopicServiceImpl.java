package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.dto.TopicDto;
import com.euler.system.api.RemoteAuditKeywordService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import com.euler.community.domain.bo.TopicBo;
import com.euler.community.domain.vo.TopicVo;
import com.euler.community.domain.entity.Topic;
import com.euler.community.mapper.TopicMapper;
import com.euler.community.service.ITopicService;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@RequiredArgsConstructor
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper,Topic> implements ITopicService {

    private final TopicMapper baseMapper;

    @DubboReference
    private RemoteAuditKeywordService remoteAuditKeywordService;

    /**
     * 查询话题
     *
     * @param id 话题主键
     * @return 话题
     */
    @Override
    public TopicVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询话题列表
     *
     * @param dto 话题
     * @return 话题
     */
    @Override
    public TableDataInfo<TopicVo> queryPageList(TopicDto dto) {
        LambdaQueryWrapper<Topic> lqw = buildQueryWrapper(dto);
        Page<TopicVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        boolean isExsitEq = false;
        boolean isRecordEmpty = true;
        Topic entity = new Topic();
        // 设置缓存的key
        String key = Constants.COMMUNITY_KEY + "topic:";
        if (result != null && result.getRecords() != null && !result.getRecords().isEmpty()) {
            isRecordEmpty = false;
            for (TopicVo topic : result.getRecords()) {
                if (StringUtils.isNotBlank(dto.getTopicName())
                    && StringUtils.equals(topic.getTopicName(), dto.getTopicName())) {
                    // 设置标志符，不需要再创建话题
                    isExsitEq = true;

                    key = key + topic.getId();
                    // 话题访问次数, 自增+1
                    topic.setSearchCount(RedisUtils.incrAtomicValue(key));
                    entity = BeanUtil.toBean(topic, Topic.class);
                    break;
                }
            }
        }

        if (!isExsitEq) {
            TopicVo newVo = new TopicVo();
            newVo.setMemberId(LoginHelper.getUserId());
            newVo.setTopicName(dto.getTopicName());
            newVo.setStatus("0");
            newVo.setNeedAdd(1);
            if (isRecordEmpty) {
                List<TopicVo> list = new ArrayList<>();
                list.add(newVo);
                result.setRecords(list);
                result.setTotal(list.size());
            } else {
                result.getRecords().add(0, newVo);
            }
        } else {
            // 更新话题访问次数
            baseMapper.updateById(entity);
        }


        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<Topic> buildQueryWrapper(TopicDto dto) {
        LambdaQueryWrapper<Topic> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(dto.getTopicName()), Topic::getTopicName, dto.getTopicName());
        lqw.eq(StringUtils.isNotBlank(dto.getStatus()), Topic::getStatus, dto.getStatus());
        lqw.orderByAsc(Topic::getTopicName);
        return lqw;
    }

    /**
     * 新增话题
     *
     * @param bo 话题
     * @return 结果
     */
    @Override
    public R insertByBo(TopicBo bo) {
        Topic add = BeanUtil.toBean(bo, Topic.class);
        String result = validEntityBeforeSave(add);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        // 默认已发布
        add.setStatus("1");
        // 判断该话题是否存在
        boolean existsFlag = baseMapper.exists(new LambdaQueryWrapper<Topic>()
            .eq(Topic::getTopicName, bo.getTopicName()));

        if(!existsFlag) {
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
            return R.ok(add);
        }
        return R.fail("该话题已存在");
    }

    /**
     * 修改话题
     *
     * @param bo 话题
     * @return 结果
     */
    @Override
    public R updateByBo(TopicBo bo) {
        if (bo.getId() == null || bo.getId() <= 0) {
            return R.fail("参数缺失");
        }
        Topic update = BeanUtil.toBean(bo, Topic.class);
        String result = validEntityBeforeSave(update);
        if (!result.equals("success")) {
            return R.fail(result);
        }
        // 默认已发布
        update.setStatus("1");
        int i = baseMapper.updateById(update);
        if (i > 0) {
            return R.ok(update);
        }
        return R.fail("修改失败");
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private String validEntityBeforeSave(Topic entity) {
        R checkKeyword = remoteAuditKeywordService.systemCheck(entity.getTopicName(), 1);
        if (checkKeyword.getCode() == R.FAIL) {
            return "话题存在敏感词";
        }
        return "success";
    }


    /**
     * 查询热门话题列表
     *
     * @param dto 话题
     * @return 话题
     */
    @Override
    public TableDataInfo<TopicVo> queryList(TopicDto dto) {
        // 根据搜索次数对数据库表中数据进行降序排列
        LambdaQueryWrapper<Topic> lqw = Wrappers.lambdaQuery();
        lqw.likeRight(StringUtils.isNotBlank(dto.getTopicName()), Topic::getTopicName, dto.getTopicName());
        lqw.orderByDesc(Topic::getSearchCount);
        List<TopicVo> list = baseMapper.selectVoList(lqw);

        List newList = new ArrayList();
        // 默认取最新的10个话题
        if (list != null && list.size() > 10) {
            newList = list.subList(0, 10);
        } else {
            newList = list;
        }
        TableDataInfo<TopicVo> build = TableDataInfo.build(newList);
        build.setTotal(newList.size());
        build.setRows(newList);
        return build;
    }

}
