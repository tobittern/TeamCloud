package com.euler.community.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.DynamicOperationErrorLogBo;
import com.euler.community.domain.entity.DynamicOperationErrorLog;
import com.euler.community.domain.vo.DynamicOperationErrorLogVo;
import com.euler.community.mapper.DynamicOperationErrorLogMapper;
import com.euler.community.service.IDynamicOperationErrorLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 动态操作错误日志Service业务层处理
 *
 * @author euler
 * @date 2022-06-06
 */
@RequiredArgsConstructor
@Service
public class DynamicOperationErrorLogServiceImpl extends ServiceImpl<DynamicOperationErrorLogMapper,DynamicOperationErrorLog> implements IDynamicOperationErrorLogService {

    private final DynamicOperationErrorLogMapper baseMapper;

    /**
     * 查询动态操作错误日志
     *
     * @param id 动态操作错误日志主键
     * @return 动态操作错误日志
     */
    @Override
    public DynamicOperationErrorLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询动态操作错误日志列表
     *
     * @param bo 动态操作错误日志
     * @return 动态操作错误日志
     */
    @Override
    public TableDataInfo<DynamicOperationErrorLogVo> queryPageList(DynamicOperationErrorLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DynamicOperationErrorLog> lqw = buildQueryWrapper(bo);
        Page<DynamicOperationErrorLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询动态操作错误日志列表
     *
     * @param bo 动态操作错误日志
     * @return 动态操作错误日志
     */
    @Override
    public List<DynamicOperationErrorLogVo> queryList(DynamicOperationErrorLogBo bo) {
        LambdaQueryWrapper<DynamicOperationErrorLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DynamicOperationErrorLog> buildQueryWrapper(DynamicOperationErrorLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DynamicOperationErrorLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDynamicId() != null, DynamicOperationErrorLog::getDynamicId, bo.getDynamicId());
        lqw.eq(bo.getOperationType() != null, DynamicOperationErrorLog::getOperationType, bo.getOperationType());
        return lqw;
    }

    /**
     * 新增动态操作错误日志
     *
     * @param bo 动态操作错误日志
     * @return 结果
     */
    @Override
    public Boolean insertByBo(DynamicOperationErrorLogBo bo) {
        DynamicOperationErrorLog add = BeanUtil.toBean(bo, DynamicOperationErrorLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改动态操作错误日志
     *
     * @param bo 动态操作错误日志
     * @return 结果
     */
    @Override
    public Boolean updateByBo(DynamicOperationErrorLogBo bo) {
        DynamicOperationErrorLog update = BeanUtil.toBean(bo, DynamicOperationErrorLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(DynamicOperationErrorLog entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除动态操作错误日志
     *
     * @param ids 需要删除的动态操作错误日志主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
