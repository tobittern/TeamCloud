package com.euler.platform.service;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.domain.bo.OpenUserCertificationAuditRecordBo;
import com.euler.platform.domain.bo.OpenUserCertificationBo;
import com.euler.platform.domain.dto.OpenUserCertificationPageDto;
import com.euler.platform.domain.vo.OpenUserCertificationDataCountVo;
import com.euler.platform.domain.vo.OpenUserCertificationVo;

import java.util.Collection;

/**
 * 用户的资质认证记录Service接口
 *
 * @author open
 * @date 2022-02-23
 */
public interface IOpenUserCertificationService {

    /**
     * 资质审核基础数据统计
     *
     */
    OpenUserCertificationDataCountVo selectCount();

    /**
     * 查询用户的资质认证记录
     *
     * @return 用户的资质认证记录
     */
    OpenUserCertificationVo selectInfo(Integer id, Long userId);

    /**
     * 查询用户的资质认证记录列表
     *
     * @return 用户的资质认证记录集合
     */
    TableDataInfo<OpenUserCertificationVo> queryPageList(OpenUserCertificationPageDto openUserAuthPageDto);


    /**
     * 修改用户的资质认证记录
     *
     * @return 结果
     */
    R insertByBo(OpenUserCertificationBo bo);

    /**
     * 修改用户的资质认证记录
     *
     * @return 结果
     */
    R updateByBo(OpenUserCertificationBo bo);


    /**
     * 校验并批量删除用户的资质认证记录信息
     *
     * @param ids 需要删除的用户的资质认证记录主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Integer> ids, Boolean isValid);

    /**
     * 撤销审核
     * @param userId
     * @return
     */
    R revokeApproval(Long userId);

    /**
     * 用户认证信息的审核
     * @param bo
     * @return
     */
    R auditUserCertification(OpenUserCertificationAuditRecordBo bo);

    /**
     * 查询
     * @param dto
     * @return
     */
    R search(OpenUserCertificationPageDto dto);
}
