package com.euler.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.core.utils.DateUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.euler.payment.domain.AliComplaintFeedback;
import com.euler.payment.domain.dto.AliComplaintFeedbackPageDto;
import com.euler.payment.domain.vo.AliComplaintFeedbackVo;
import com.euler.payment.mapper.AliComplaintFeedbackMapper;
import com.euler.payment.service.IAliComplaintFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Collection;

/**
 * 商家处理交易投诉Service业务层处理
 *
 * @author euler
 * @date 2022-10-18
 */
@RequiredArgsConstructor
@Service
public class AliComplaintFeedbackServiceImpl extends ServiceImpl<AliComplaintFeedbackMapper, AliComplaintFeedback> implements IAliComplaintFeedbackService {

    @Autowired
    private AliComplaintFeedbackMapper baseMapper;

    /**
     * 查询商家处理交易投诉
     *
     * @param id 商家处理交易投诉主键
     * @return 商家处理交易投诉
     */
    @Override
    public AliComplaintFeedbackVo queryById(Integer id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询商家处理交易投诉列表
     *
     * @param pageDto 商家处理交易投诉
     * @return 商家处理交易投诉
     */
    @Override
    public TableDataInfo<AliComplaintFeedbackVo> queryPageList(AliComplaintFeedbackPageDto pageDto) {
        LambdaQueryWrapper<AliComplaintFeedback> lqw = buildQueryWrapper(pageDto);
        Page<AliComplaintFeedbackVo> result = baseMapper.selectVoPage(pageDto.build(), lqw);
        return TableDataInfo.build(result);
    }


    /**
     * 查询商家处理交易投诉列表
     *
     * @param pageDto 商家处理交易投诉
     * @return 商家处理交易投诉
     */
    @Override
    public List<AliComplaintFeedbackVo> queryList(AliComplaintFeedbackPageDto pageDto) {
        LambdaQueryWrapper<AliComplaintFeedback> lqw = buildQueryWrapper(pageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<AliComplaintFeedback> buildQueryWrapper(AliComplaintFeedbackPageDto pageDto) {
        LambdaQueryWrapper<AliComplaintFeedback> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(pageDto.getComplainEventId()), AliComplaintFeedback::getComplainEventId, pageDto.getComplainEventId());
        lqw.eq(StringUtils.isNotBlank(pageDto.getFeedbackCode()), AliComplaintFeedback::getFeedbackCode, pageDto.getFeedbackCode());
        lqw.eq(StringUtils.isNotBlank(pageDto.getFeedbackContent()), AliComplaintFeedback::getFeedbackContent, pageDto.getFeedbackContent());
        lqw.eq(StringUtils.isNotBlank(pageDto.getFeedbackImages()), AliComplaintFeedback::getFeedbackImages, pageDto.getFeedbackImages());
        lqw.eq(StringUtils.isNotBlank(pageDto.getOperator()), AliComplaintFeedback::getOperator, pageDto.getOperator());
        lqw.ge(StringUtils.isNotBlank(pageDto.getBeginTime()), AliComplaintFeedback::getCreateTime, DateUtils.getBeginOfDay(pageDto.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(pageDto.getEndTime()), AliComplaintFeedback::getCreateTime, DateUtils.getEndOfDay(pageDto.getEndTime()));
        return lqw;
    }

    /**
     * 新增商家处理交易投诉
     *
     * @return 结果
     */
    @Override
    public Boolean insertByBo(AliComplaintFeedback entity) {
        boolean flag = baseMapper.insert(entity) > 0;
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 修改商家处理交易投诉
     *
     * @return 结果
     */
    @Override
    public Boolean updateByBo(AliComplaintFeedback entity) {
        return baseMapper.updateById(entity) > 0;
    }


    /**
     * 批量删除商家处理交易投诉
     *
     * @param ids 需要删除的商家处理交易投诉主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Integer> ids) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
