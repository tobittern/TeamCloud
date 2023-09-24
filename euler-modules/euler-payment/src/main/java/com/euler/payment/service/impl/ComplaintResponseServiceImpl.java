package com.euler.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.ComplaintResponse;
import com.euler.payment.domain.dto.ComplaintResponsePageDto;
import com.euler.payment.domain.vo.ComplaintResponseVo;
import com.euler.payment.mapper.ComplaintResponseMapper;
import com.euler.payment.service.IComplaintResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 投诉回复Service业务层处理
 *
 * @author euler
 * @date 2022-09-13
 */
@RequiredArgsConstructor
@Service
public class ComplaintResponseServiceImpl extends ServiceImpl<ComplaintResponseMapper, ComplaintResponse> implements IComplaintResponseService {

    @Autowired
    private ComplaintResponseMapper baseMapper;

    /**
     * 查询投诉回复
     *
     * @param id 投诉回复主键
     * @return 投诉回复
     */
    @Override
    public ComplaintResponseVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询投诉回复列表
     *
     * @param pageDto 投诉回复
     * @return 投诉回复
     */
    @Override
    public TableDataInfo<ComplaintResponseVo> queryPageList(ComplaintResponsePageDto pageDto) {
        LambdaQueryWrapper<ComplaintResponse> lqw = buildQueryWrapper(pageDto);
        Page<ComplaintResponseVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询投诉回复列表
     *
     * @param pageDto 投诉回复
     * @return 投诉回复
     */
    @Override
    public List<ComplaintResponseVo> queryList(ComplaintResponsePageDto pageDto) {
        LambdaQueryWrapper<ComplaintResponse> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ComplaintResponse> buildQueryWrapper(ComplaintResponsePageDto pageDto) {
        LambdaQueryWrapper<ComplaintResponse> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplaintId()), ComplaintResponse::getComplaintId, pageDto.getComplaintId());
        lqw.likeRight(StringUtils.isNotBlank(pageDto.getResponseContent()), ComplaintResponse::getResponseContent, pageDto.getResponseContent());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), ComplaintResponse::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), ComplaintResponse::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }

    /**
     * 新增投诉回复
     *
     * @param entity 投诉回复
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ComplaintResponse entity) {
        boolean flag = baseMapper.insert(entity) > 0;
        if (flag) {
            return true;
        }
        return false;
    }


    /**
     * 批量删除投诉回复
     *
     * @param ids 需要删除的投诉回复主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
