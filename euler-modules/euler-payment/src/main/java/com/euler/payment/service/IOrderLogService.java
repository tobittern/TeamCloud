package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.domain.OrderLog;
import com.euler.payment.domain.dto.OrderLogPageDto;
import com.euler.payment.domain.vo.OrderLogVo;

import java.util.List;

/**
 * 订单日志Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IOrderLogService extends IService<OrderLog> {

    /**
     * 查询订单日志
     *
     * @param orderId 订单id
     * @return 订单日志
     */
    OrderLogVo queryById(Long orderId);

    /**
     * 查询订单日志列表
     *
     * @param orderLogPageDto 订单日志
     * @return 订单日志集合
     */
    TableDataInfo<OrderLogVo> queryPageList(OrderLogPageDto orderLogPageDto);

    /**
     * 查询订单日志列表
     *
     * @param orderLogPageDto 订单日志
     * @return 订单日志集合
     */
    List<OrderLogVo> queryList(OrderLogPageDto orderLogPageDto);
    /**
     * 查询订单日志列表
     *
     * @param orderId
     * @return 订单日志集合
     */
    List<OrderLogVo> queryListByOrderId(String orderId);

}
