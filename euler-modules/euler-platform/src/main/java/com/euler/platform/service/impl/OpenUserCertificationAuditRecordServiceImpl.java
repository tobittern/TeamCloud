package com.euler.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.PageQuery;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.platform.domain.OpenUserCertificationAuditRecord;
import com.euler.platform.domain.bo.OpenUserCertificationAuditRecordBo;
import com.euler.platform.domain.dto.CommonIdPageDto;
import com.euler.platform.domain.vo.OpenUserCertificationAuditRecordVo;
import com.euler.platform.mapper.OpenUserCertificationAuditRecordMapper;
import com.euler.platform.service.IOpenUserCertificationAuditRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户的认证的审核记录Service业务层处理
 *
 * @author open
 * @date 2022-02-23
 */
@RequiredArgsConstructor
@Service
public class OpenUserCertificationAuditRecordServiceImpl implements IOpenUserCertificationAuditRecordService {

    private final OpenUserCertificationAuditRecordMapper baseMapper;


    /**
     * 查询用户的认证的审核记录
     *
     * @param id 用户的认证的审核记录主键
     * @return 用户的认证的审核记录
     */
    @Override
    public OpenUserCertificationAuditRecordVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户的认证的审核记录列表
     *
     * @return 用户的认证的审核记录
     */
    @Override
    public TableDataInfo<OpenUserCertificationAuditRecordVo> queryPageList(OpenUserCertificationAuditRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OpenUserCertificationAuditRecord> lqw = buildQueryWrapper(bo);
        Page<OpenUserCertificationAuditRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<OpenUserCertificationAuditRecord> buildQueryWrapper(OpenUserCertificationAuditRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OpenUserCertificationAuditRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAuditUserId() != null, OpenUserCertificationAuditRecord::getAuditUserId, bo.getAuditUserId());
        lqw.eq(bo.getUserAuthId() != null, OpenUserCertificationAuditRecord::getUserAuthId, bo.getUserAuthId());
        lqw.eq(bo.getAuditStatus() != null, OpenUserCertificationAuditRecord::getAuditStatus, bo.getAuditStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getAuditRecord()), OpenUserCertificationAuditRecord::getAuditRecord, bo.getAuditRecord());
        return lqw;
    }

    /**
     * 新增用户的认证的审核记录
     *
     * @return 结果
     */
    @Override
    public Boolean insertByBo(OpenUserCertificationAuditRecordBo bo) {
        OpenUserCertificationAuditRecord add = BeanUtil.toBean(bo, OpenUserCertificationAuditRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 获取审核列表
     *
     * @param id
     * @return
     */
    @Override
    public TableDataInfo<OpenUserCertificationAuditRecordVo> auditUserCertificationList(CommonIdPageDto dto) {
        LambdaQueryWrapper<OpenUserCertificationAuditRecord> eq = Wrappers.<OpenUserCertificationAuditRecord>lambdaQuery()
            .eq(OpenUserCertificationAuditRecord::getUserAuthId, dto.getId())
            .orderByDesc(OpenUserCertificationAuditRecord::getId);

        IPage<OpenUserCertificationAuditRecordVo> result = baseMapper.selectVoPage(dto.build(), eq);
        return TableDataInfo.build(result);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(OpenUserCertificationAuditRecord entity) {
        //TODO 做一些数据校验,如唯一约束
    }


}
