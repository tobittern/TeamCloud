package com.euler.platform.service;

import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.domain.bo.OpenGameAuditRecordBo;
import com.euler.platform.domain.dto.CommonIdPageDto;
import com.euler.platform.domain.vo.OpenGameAuditRecordVo;

/**
 * Service接口
 *
 * @author open
 * @date 2022-02-21
 */
public interface IOpenGameAuditRecordService {

    /**
     * 查询列表
     *
     * @param id 主键
     * @return
     */
    TableDataInfo<OpenGameAuditRecordVo> auditGameList(CommonIdPageDto dto);

    /**
     * 游戏审核记录添加
     *
     * @param bo
     * @return
     */
    Boolean insertByBo(OpenGameAuditRecordBo bo);


}
