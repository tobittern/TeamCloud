package com.euler.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.OrderLog;
import com.euler.payment.domain.dto.OrderLogPageDto;
import com.euler.payment.domain.vo.OrderLogVo;
import com.euler.payment.mapper.OrderLogMapper;
import com.euler.payment.service.IOrderLogService;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 订单日志Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@RequiredArgsConstructor
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements IOrderLogService {

    private final OrderLogMapper baseMapper;

    /**
     * 查询订单日志
     *
     * @param id 订单日志主键
     * @return 订单日志
     */
    @Override
    public OrderLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询订单日志列表
     *
     * @param bo 订单日志
     * @return 订单日志
     */
    @Override
    public TableDataInfo<OrderLogVo> queryPageList(OrderLogPageDto orderLogPageDto) {
        LambdaQueryWrapper<OrderLog> lqw = buildQueryWrapper(orderLogPageDto);
        Page<OrderLogVo> result = baseMapper.selectVoPage(orderLogPageDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单日志列表
     *
     * @param orderLogPageDto 订单日志
     * @return 订单日志
     */
    @Override
    public List<OrderLogVo> queryList(OrderLogPageDto orderLogPageDto) {
        LambdaQueryWrapper<OrderLog> lqw = buildQueryWrapper(orderLogPageDto);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询订单日志列表
     *
     * @param orderId
     * @return 订单日志集合
     */
    @Override
    public List<OrderLogVo> queryListByOrderId(String orderId) {
        OrderLogPageDto orderLogPageDto = new OrderLogPageDto();
        orderLogPageDto.setBusinessOrderId(orderId);
        LambdaQueryWrapper<OrderLog> lqw = buildQueryWrapper(orderLogPageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OrderLog> buildQueryWrapper(OrderLogPageDto bo) {
        LambdaQueryWrapper<OrderLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrderLog::getBusinessOrderId, bo.getBusinessOrderId());
        lqw.orderByAsc(OrderLog::getId);
        return lqw;
    }


}
