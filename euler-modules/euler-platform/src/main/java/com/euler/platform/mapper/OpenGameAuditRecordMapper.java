package com.euler.platform.mapper;

import com.euler.common.mybatis.core.mapper.BaseMapperPlus;
import com.euler.platform.domain.OpenGameAuditRecord;
import com.euler.platform.domain.vo.OpenGameAuditRecordVo;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * Mapper接口
 *
 * @author open
 * @date 2022-02-21
 */
public interface OpenGameAuditRecordMapper extends BaseMapperPlus<OpenGameAuditRecordMapper, OpenGameAuditRecord, OpenGameAuditRecordVo> {

    List<OpenGameAuditRecord> selectAuditNewOne(@Param("gameIds") List<Integer> gameIds);

}
