package com.euler.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.community.domain.bo.DynamicOperationErrorLogBo;
import com.euler.community.domain.entity.DynamicOperationErrorLog;
import com.euler.community.domain.vo.DynamicOperationErrorLogVo;

import java.util.Collection;
import java.util.List;

/**
 * 动态操作错误日志Service接口
 *
 * @author euler
 * @date 2022-06-06
 */
public interface IDynamicOperationErrorLogService extends IService<DynamicOperationErrorLog> {

    /**
     * 查询动态操作错误日志
     *
     * @param id 动态操作错误日志主键
     * @return 动态操作错误日志
     */
    DynamicOperationErrorLogVo queryById(Long id);

    /**
     * 查询动态操作错误日志列表
     *
     * @return 动态操作错误日志集合
     */
    TableDataInfo<DynamicOperationErrorLogVo> queryPageList(DynamicOperationErrorLogBo bo, PageQuery pageQuery);

    /**
     * 查询动态操作错误日志列表
     *
     * @return 动态操作错误日志集合
     */
    List<DynamicOperationErrorLogVo> queryList(DynamicOperationErrorLogBo bo);

    /**
     * 修改动态操作错误日志
     *
     * @return 结果
     */
    Boolean insertByBo(DynamicOperationErrorLogBo bo);

    /**
     * 修改动态操作错误日志
     *
     * @return 结果
     */
    Boolean updateByBo(DynamicOperationErrorLogBo bo);

    /**
     * 校验并批量删除动态操作错误日志信息
     *
     * @param ids     需要删除的动态操作错误日志主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
