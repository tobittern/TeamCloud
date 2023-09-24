package com.euler.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.DynamicOperationLogDto;
import com.euler.community.domain.entity.DynamicOperationLog;
import com.euler.community.domain.vo.DynamicOperationLogVo;
import com.euler.community.mapper.DynamicOperationLogMapper;
import com.euler.community.service.IDynamicOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 动态操作错误日志Service业务层处理
 *
 * @author euler
 * @date 2022-06-20
 */
@RequiredArgsConstructor
@Service
public class DynamicOperationLogServiceImpl extends ServiceImpl<DynamicOperationLogMapper,DynamicOperationLog> implements IDynamicOperationLogService {

    private final DynamicOperationLogMapper baseMapper;


    /**
     * 查询动态操作错误日志列表
     *
     * @return 动态操作错误日志
     */
    @Override
    public TableDataInfo<DynamicOperationLogVo> queryPageList(DynamicOperationLogDto dto) {
        LambdaQueryWrapper<DynamicOperationLog> lqw = buildQueryWrapper(dto);
        Page<DynamicOperationLogVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<DynamicOperationLog> buildQueryWrapper(DynamicOperationLogDto dto) {
        LambdaQueryWrapper<DynamicOperationLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMemberId() != null, DynamicOperationLog::getMemberId, dto.getMemberId());
        lqw.eq(dto.getDynamicId() != null, DynamicOperationLog::getDynamicId, dto.getDynamicId());
        lqw.eq(dto.getOperationType() != null, DynamicOperationLog::getOperationType, dto.getOperationType());
        lqw.eq(StringUtils.isNotBlank(dto.getOperationContent()), DynamicOperationLog::getOperationContent, dto.getOperationContent());
        lqw.orderByDesc(DynamicOperationLog::getId);
        return lqw;
    }
}
