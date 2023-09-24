package com.euler.system.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.KeyValueDto;
import com.euler.system.domain.dto.SysAuditKeywordDto;
import com.euler.system.domain.vo.SysAuditKeywordVo;
import com.euler.system.domain.bo.SysAuditKeywordBo;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 审核关键词 - 敏感词Service接口
 *
 * @author euler
 * @date 2022-03-21
 */
public interface ISysAuditKeywordService {

    /**
     * 查询审核关键词 - 敏感词列表
     *
     * @return 审核关键词 - 敏感词集合
     */
    TableDataInfo<SysAuditKeywordVo> queryPageList(SysAuditKeywordDto sysAuditKeywordDto);

    /**
     * 查询审核关键词 - 敏感词列表
     *
     * @return 审核关键词 - 敏感词集合
     */
    List<SysAuditKeywordVo> queryList(SysAuditKeywordDto sysAuditKeywordDto);

    /**
     * 修改审核关键词 - 敏感词
     *
     * @return 结果
     */
    R insertByBo(SysAuditKeywordBo bo);

    /**
     * 修改审核关键词 - 敏感词
     *
     * @return 结果
     */
    R updateByBo(SysAuditKeywordBo bo);

    /**
     * 校验并批量删除审核关键词 - 敏感词信息
     *
     * @param ids 需要删除的审核关键词 - 敏感词主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 判断关键词是否是敏感词
     *
     * @return 结果
     */
    R check(String checkString, Integer type);
}
