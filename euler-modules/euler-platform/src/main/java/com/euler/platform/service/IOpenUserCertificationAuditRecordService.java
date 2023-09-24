package com.euler.platform.service;

import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.domain.bo.OpenUserCertificationAuditRecordBo;
import com.euler.platform.domain.dto.CommonIdPageDto;
import com.euler.platform.domain.vo.OpenUserCertificationAuditRecordVo;

/**
 * 用户的认证的审核记录Service接口
 *
 * @author open
 * @date 2022-02-23
 */
public interface IOpenUserCertificationAuditRecordService {

    /**
     * 查询用户的认证的审核记录
     *
     * @param id 用户的认证的审核记录主键
     * @return 用户的认证的审核记录
     */
    OpenUserCertificationAuditRecordVo queryById(Integer id);

    /**
     * 查询用户的认证的审核记录列表
     *
     * @return 用户的认证的审核记录集合
     */
    TableDataInfo<OpenUserCertificationAuditRecordVo> queryPageList(OpenUserCertificationAuditRecordBo bo, PageQuery pageQuery);

    /**
     * 修改用户的认证的审核记录
     *
     * @return 结果
     */
    Boolean insertByBo(OpenUserCertificationAuditRecordBo bo);

    /**
     * 获取审核列表
     * @param id
     * @return
     */
    TableDataInfo<OpenUserCertificationAuditRecordVo> auditUserCertificationList(CommonIdPageDto dto);


}
