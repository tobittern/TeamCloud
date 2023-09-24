package com.euler.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.domain.OpenGameAuditRecord;
import com.euler.platform.domain.bo.OpenGameAuditRecordBo;
import com.euler.platform.domain.dto.CommonIdPageDto;
import com.euler.platform.domain.vo.OpenGameAuditRecordVo;
import com.euler.platform.mapper.OpenGameAuditRecordMapper;
import com.euler.platform.service.IOpenGameAuditRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service业务层处理
 *
 * @author open
 * @date 2022-02-21
 */
@RequiredArgsConstructor
@Service
public class OpenGameAuditRecordServiceImpl implements IOpenGameAuditRecordService {

    private final OpenGameAuditRecordMapper baseMapper;

    /**
     * 查询
     *
     * @param 主键
     * @return
     */
    @Override
    public TableDataInfo<OpenGameAuditRecordVo> auditGameList(CommonIdPageDto dto) {

        LambdaQueryWrapper<OpenGameAuditRecord> eq = Wrappers.<OpenGameAuditRecord>lambdaQuery()
            .eq(OpenGameAuditRecord::getGameId, dto.getId())
            .orderByDesc(OpenGameAuditRecord::getId);

        Page<OpenGameAuditRecordVo> result = baseMapper.selectVoPage(dto.build(), eq);

        return TableDataInfo.build(result);
    }



    /**
     * 新增
     *
     * @param
     * @return 结果
     */
    @Override
    public Boolean insertByBo(OpenGameAuditRecordBo bo) {
        OpenGameAuditRecord add = BeanUtil.toBean(bo, OpenGameAuditRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(OpenGameAuditRecord entity){
        //TODO 做一些数据校验,如唯一约束
    }

}
