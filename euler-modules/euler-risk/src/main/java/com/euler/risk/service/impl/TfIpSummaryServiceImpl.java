package com.euler.risk.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.core.domain.dto.FillDataDto;
import com.euler.risk.domain.dto.IpSearchDto;
import com.euler.risk.domain.entity.TfIpSummary;
import com.euler.risk.domain.vo.TfIpSummaryVo;
import com.euler.risk.mapper.TfIpSummaryMapper;
import com.euler.risk.service.ITfIpSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ip汇总Service业务层处理
 *
 * @author euler
 * @date 2022-08-24
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TfIpSummaryServiceImpl extends ServiceImpl<TfIpSummaryMapper, TfIpSummary> implements ITfIpSummaryService {

    @Autowired
    private TfIpSummaryMapper baseMapper;

    /**
     * 查询行为异常预警列表
     *
     * @param dto ip检索条件
     * @return ip行为异常预警列表
     */
    @Override
    public TableDataInfo<TfIpSummaryVo> queryPageList(IpSearchDto dto) {
        LambdaQueryWrapper<TfIpSummary> lqw = buildQueryWrapper(dto);
        Page<TfIpSummaryVo> result = baseMapper.selectIpSummaryList(dto.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<TfIpSummary> buildQueryWrapper(IpSearchDto dto) {
        LambdaQueryWrapper<TfIpSummary> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getIp()), TfIpSummary::getIp, dto.getIp());
        lqw.ge(dto.getStartTime() != null, TfIpSummary::getDateId, dto.getStartTime());
        lqw.le(dto.getEndTime() != null, TfIpSummary::getDateId, dto.getEndTime());
        lqw.ge(dto.getRegisterStartNum() != null, TfIpSummary::getRegisterNum, dto.getRegisterStartNum());
        lqw.le(dto.getRegisterEndNum() != null, TfIpSummary::getRegisterNum, dto.getRegisterEndNum());
        lqw.ge(dto.getRoleCreateStartNum() != null, TfIpSummary::getRoleCreateNum, dto.getRoleCreateStartNum());
        lqw.le(dto.getRoleCreateEndNum() != null, TfIpSummary::getRoleCreateNum, dto.getRoleCreateEndNum());
        lqw.ge(dto.getLoginStartNum() != null, TfIpSummary::getLoginNum, dto.getLoginStartNum());
        lqw.le(dto.getLoginEndNum() != null, TfIpSummary::getLoginNum, dto.getLoginEndNum());
        lqw.orderByAsc(TfIpSummary::getRegisterNum);
        lqw.orderByAsc(TfIpSummary::getRoleCreateNum);
        lqw.orderByAsc(TfIpSummary::getLoginNum);
        lqw.groupBy(TfIpSummary::getIp);
        return lqw;
    }


    /**
     * 定时统计IP数据
     *
     * @param fillDataDto
     */
    @Override
    public void fillData(FillDataDto fillDataDto) {
        //清除数据
        log.info("定时统计IP数据--清除数据--开始，batchNo:{}", fillDataDto.getBatchNo());

        var wrapper = new LambdaQueryWrapper<TfIpSummary>();
        wrapper.between(TfIpSummary::getDateId, fillDataDto.getBeginTime(), fillDataDto.getEndTime());
        Integer res = baseMapper.delete(wrapper);
        log.info("定时统计IP数据--清除数据--清除数据量，batchNo：{}，res：{}", fillDataDto.getBatchNo(), res);

        log.info("定时统计IP数据--填充数据--开始，batchNo:{}", fillDataDto.getBatchNo());
        baseMapper.fillData(fillDataDto);
        log.info("定时统计IP数据--填充数据--结束，batchNo:{}", fillDataDto.getBatchNo());

    }

}
