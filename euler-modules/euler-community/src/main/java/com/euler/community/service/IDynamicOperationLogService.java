package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.dto.DynamicOperationLogDto;
import com.euler.community.domain.entity.DynamicOperationLog;
import com.euler.community.domain.vo.DynamicOperationLogVo;

/**
 * 动态操作错误日志Service接口
 *
 * @author euler
 * @date 2022-06-20
 */
public interface IDynamicOperationLogService extends IService<DynamicOperationLog> {


    /**
     * 查询动态操作错误日志列表
     *
     * @return 动态操作错误日志集合
     */
    TableDataInfo<DynamicOperationLogVo> queryPageList(DynamicOperationLogDto dto);

}
