package com.euler.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.core.domain.dto.IdTypeDto;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.community.domain.dto.CollectDto;
import com.euler.community.domain.entity.Collect;
import com.euler.community.domain.vo.CollectVo;
import com.euler.community.enums.DynamicFieldIncrEnum;
import com.euler.community.mapper.CollectMapper;
import com.euler.community.service.ICollectService;
import com.euler.community.service.IDynamicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态收藏Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@RequiredArgsConstructor
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {

    private final CollectMapper baseMapper;
    @Autowired
    private IDynamicService iDynamicService;

    /**
     * 查询动态收藏列表
     *
     * @return 动态收藏
     */
    @Override
    public TableDataInfo<CollectVo> queryPageList(CollectDto dto) {
        QueryWrapper<Collect> lqw = Wrappers.query();
        lqw.eq(dto.getDynamicId() != null, "c.dynamic_id", dto.getDynamicId());
        lqw.eq(dto.getMemberId() != null, "c.member_id", dto.getMemberId());
        lqw.eq("c.status", "1").eq("d.del_flag", "0");
        // 按照ID倒序排列
        lqw.orderByAsc("c.id");
        Page<CollectVo> result = baseMapper.getUserCollectDynamicList(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 新增动态收藏
     *
     * @return 结果
     */
    @Override
    public R insertByBo(IdNameTypeDicDto<Long> dto) {
        // 判断一下动态类型是否正确
        Integer[] collectTypeList = new Integer[]{1, 2};
        Optional<Integer> collectTypeAny = Arrays.stream(collectTypeList).filter(a -> a.equals(dto.getType())).findAny();
        if (!collectTypeAny.isPresent()) {
            return R.fail("收藏状态错误");
        }
        Long userId = LoginHelper.getUserId();
        // 首先判断是否存在
        LambdaQueryWrapper<Collect> eq = Wrappers.<Collect>lambdaQuery()
            .eq(Collect::getMemberId, userId)
            .eq(Collect::getDynamicId, dto.getId());
        Collect collect = baseMapper.selectOne(eq);
        int row = 0;
        if (collect == null) {
            // 为空的时候代表我们之前没有收藏
            Collect insertEntity = new Collect();
            insertEntity.setMemberId(userId);
            insertEntity.setDynamicId(dto.getId());
            row = baseMapper.insert(insertEntity);
        } else {
            // 不为空的时候我们需要判断当前执行的值是否和数据库存储的值一致
            if (collect.getStatus().equals(dto.getType().toString())) {
                return R.fail("您已经操作成功");
            }
            Collect updateEntity = new Collect();
            updateEntity.setId(collect.getId());
            updateEntity.setStatus(dto.getType().toString());
            row = baseMapper.updateById(updateEntity);
        }
        if (row > 0) {
            // 更新动态表和Es中的数据
            IdTypeDto<String, Integer> idTypeDto = new IdTypeDto<>();
            idTypeDto.setId(dto.getId().toString());
            idTypeDto.setType(dto.getType().equals(1) ? DynamicFieldIncrEnum.COLLECTION.getCode() : DynamicFieldIncrEnum.CANCEL_COLLECTION.getCode());
            iDynamicService.incrDynamicSomeFieldValue(idTypeDto);
            return R.ok("操作成功");
        }
        return R.fail("操作失败");
    }


    /**
     * 批量删除动态收藏
     *
     * @param ids 需要删除的动态收藏主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 检测用户是否针对一些动态进行的收藏
     *
     * @param userId
     * @param dynamicIds
     * @return
     */
    @Override
    public List<Long> checkUserIsCollect(Long userId, List<Long> dynamicIds) {
        if (dynamicIds.size() <= 0 || userId == 0L) {
            return new ArrayList<>();
        }
        // 首先查询出用户针对这些动态是否存在指定行为操作
        LambdaQueryWrapper<Collect> in = Wrappers.<Collect>lambdaQuery().eq(Collect::getMemberId, userId)
            .eq(Collect::getStatus, "1")
            .in(Collect::getDynamicId, dynamicIds);
        List<CollectVo> collectVos = baseMapper.selectVoList(in);
        if (collectVos != null && collectVos.size() > 0) {
            // 返回查询到的数据
            return collectVos.stream().map(CollectVo::getDynamicId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 统计用户收藏数量
     *
     * @return
     */
    @Override
    public Integer count(Long userId) {
        return baseMapper.getUserCollectDynamicNum(userId);
    }

    /**
     * 获取用户某天的收藏数量
     */
    @Override
    public Integer collectCountForDay(Long userId, Date date) {
        QueryWrapper<Collect> lqw = Wrappers.<Collect>query();
        lqw.eq("c.member_id", userId);
        lqw.ge("c.create_time", DateUtils.getBeginOfDay(date));
        lqw.le("c.create_time", DateUtils.getEndOfDay(date));
        lqw.eq("c.status", "1");
        lqw.eq("c.del_flag", "0");
        return baseMapper.getUserCollectCount(lqw);
    }

}
