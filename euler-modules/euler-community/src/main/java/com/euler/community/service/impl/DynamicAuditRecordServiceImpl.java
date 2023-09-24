package com.euler.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.DynamicAuditRecordDto;
import com.euler.community.domain.entity.DynamicAuditRecord;
import com.euler.community.domain.vo.DynamicAuditRecordVo;
import com.euler.community.mapper.DynamicAuditRecordMapper;
import com.euler.community.service.IDynamicAuditRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 审核记录Service业务层处理
 *
 * @author euler
 * @date 2022-06-01
 */
@RequiredArgsConstructor
@Service
public class DynamicAuditRecordServiceImpl extends ServiceImpl<DynamicAuditRecordMapper,DynamicAuditRecord> implements IDynamicAuditRecordService {

    private final DynamicAuditRecordMapper baseMapper;
    /**
     * 查询审核记录列表
     *
     * @param bo 审核记录
     * @return 审核记录
     */
    @Override
    public TableDataInfo<DynamicAuditRecordVo> queryPageList(DynamicAuditRecordDto dto) {
        LambdaQueryWrapper<DynamicAuditRecord> lqw = buildQueryWrapper(dto);
        Page<DynamicAuditRecordVo> result = baseMapper.selectVoPage(dto.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<DynamicAuditRecord> buildQueryWrapper(DynamicAuditRecordDto dto) {
        LambdaQueryWrapper<DynamicAuditRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getDynamicId() != null, DynamicAuditRecord::getDynamicId, dto.getDynamicId());
        lqw.eq(dto.getAuditId() != null, DynamicAuditRecord::getAuditId, dto.getAuditId());
        lqw.eq(dto.getAuditStatus() != null, DynamicAuditRecord::getAuditStatus, dto.getAuditStatus());
        lqw.orderByDesc(DynamicAuditRecord::getId);
        return lqw;
    }

    /**
     * 批量删除审核记录
     *
     * @param ids 需要删除的审核记录主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
