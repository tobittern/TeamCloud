package com.euler.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.domain.PayOrder;
import com.euler.payment.domain.dto.PayOrderPageDto;

import java.util.Collection;
import java.util.List;

/**
 * 支付订单Service接口
 *
 * @author euler
 * @date 2022-03-29
 */
public interface IPayOrderService extends IService<PayOrder> {

    /**
     * 查询支付订单
     *
     * @param id 支付订单主键
     * @return 支付订单
     */
    PayOrderVo queryById(String id);

    /**
     * 查询支付订单列表
     *
     * @param pageQuery 支付订单
     * @return 支付订单集合
     */
    TableDataInfo<PayOrderVo> queryPageList(PayOrderPageDto pageQuery);

    /**
     * 查询支付订单列表
     *
     * @param pageQuery 支付订单
     * @return 支付订单集合
     */
    List<PayOrderVo> queryList(PayOrderPageDto pageQuery);

    /**
     * 获取逾期未支付的订单
     *
     * @param num
     * @return
     */
    List<PayOrderVo> queryExpiredList(Integer num);

    /**
     * 获取游戏通知失败的支付单
     *
     * @param num
     * @return
     */
    List<PayOrderVo> queryGameNotifyFailList(Integer num);

    boolean updatePayOrderInfo(PayOrder payOrder);

    /**
     * 根据业务订单号获取支付订单
     *
     * @param orderId
     * @return
     */
    PayOrder getByOrderId(String orderId);

    /**
     * 校验并批量删除支付订单信息
     *
     * @param ids     需要删除的支付订单主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

}
