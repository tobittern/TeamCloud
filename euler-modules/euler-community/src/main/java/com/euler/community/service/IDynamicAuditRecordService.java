package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdNameTypeDicDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.DynamicAuditRecordDto;
import com.euler.community.domain.entity.DynamicAuditRecord;
import com.euler.community.domain.vo.DynamicAuditRecordVo;

import java.util.Collection;

/**
 * 审核记录Service接口
 *
 * @author euler
 * @date 2022-06-01
 */
public interface IDynamicAuditRecordService extends IService<DynamicAuditRecord> {

    /**
     * 查询审核记录列表
     *
     * @return 审核记录集合
     */
    TableDataInfo<DynamicAuditRecordVo> queryPageList(DynamicAuditRecordDto dto);

    /**
     * 校验并批量删除审核记录信息
     *
     * @param ids 需要删除的审核记录主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
