package com.euler.payment.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.payment.api.domain.PayOrderVo;
import com.euler.payment.domain.NotifyRecord;
import com.euler.payment.domain.PayOrder;
import com.euler.payment.domain.dto.PayOrderPageDto;
import com.euler.payment.mapper.PayOrderMapper;
import com.euler.payment.service.IPayOrderService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 支付订单Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@RequiredArgsConstructor
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    private final PayOrderMapper baseMapper;

    /**
     * 查询支付订单
     *
     * @param id 支付订单主键
     * @return 支付订单
     */
    @Override
    public PayOrderVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询支付订单列表
     *
     * @param pageQuery 支付订单
     * @return 支付订单
     */
    @Override
    public TableDataInfo<PayOrderVo> queryPageList(PayOrderPageDto pageQuery) {
        LambdaQueryWrapper<PayOrder> lqw = buildQueryWrapper(pageQuery);
        Page<PayOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 获取逾期未支付的订单
     *
     * @param num
     * @return
     */
    @Override
    public List<PayOrderVo> queryExpiredList(Integer num) {
        LambdaQueryWrapper<PayOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayOrder::getState, PayOrder.STATE_ING)
            .ge(PayOrder::getExpiredTime, DateUtil.offsetDay(new Date(), -7))
            .le(PayOrder::getExpiredTime, new Date()).last("limit " + num);
        return baseMapper.selectVoList(lqw);

    }

    /**
     * 获取游戏通知失败的支付单
     *
     * @param num
     * @return
     */
    @Override
    public List<PayOrderVo> queryGameNotifyFailList(Integer num) {
        LambdaQueryWrapper<PayOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayOrder::getState, PayOrder.STATE_SUCCESS)
            .eq(PayOrder::getNotifyState, NotifyRecord.NOTIFY_STATE_FAILED)
            .ge(PayOrder::getCreateTime, DateUtil.offsetMinute(new Date(), -35))
            .le(PayOrder::getCreateTime, new Date())
            .apply("notify_count<=notify_count_limit").last("limit " + num);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询支付订单列表
     *
     * @param bo 支付订单
     * @return 支付订单
     */
    @Override
    public List<PayOrderVo> queryList(PayOrderPageDto bo) {
        LambdaQueryWrapper<PayOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PayOrder> buildQueryWrapper(PayOrderPageDto dto) {
        LambdaQueryWrapper<PayOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getState() != null, PayOrder::getState, dto.getState());
        lqw.eq(dto.getOrderId() != null, PayOrder::getBusinessOrderId, dto.getOrderId());
        return lqw;
    }

    /**
     * 修改订单状态
     *
     * @param payOrder
     * @return
     */
    public boolean updatePayOrderInfo(PayOrder payOrder) {
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
        updateChainWrapper.eq(PayOrder::getId, payOrder.getId())
            .set(payOrder.getState() != null, PayOrder::getState, payOrder.getState())
            .set(payOrder.getRefundState() != null, PayOrder::getRefundState, payOrder.getRefundState())
            .set(payOrder.getRefundAmount() != null, PayOrder::getRefundAmount, payOrder.getRefundAmount())
            .set(payOrder.getRefundTimes() != null, PayOrder::getRefundTimes, payOrder.getRefundTimes())

            .set(payOrder.getNotifyCount() != null, PayOrder::getNotifyCount, payOrder.getNotifyCount())
            .set(payOrder.getNotifyState() != null, PayOrder::getNotifyState, payOrder.getNotifyState())
            .set(payOrder.getLastNotifyTime() != null, PayOrder::getLastNotifyTime, payOrder.getLastNotifyTime())

            .set(StringUtils.isNotEmpty(payOrder.getPayChannelOrderNo()), PayOrder::getPayChannelOrderNo, payOrder.getPayChannelOrderNo())
            .set(PayOrder::getPayChannelUser, payOrder.getPayChannelUser())
            .set(payOrder.getSuccessTime() != null, PayOrder::getSuccessTime, payOrder.getSuccessTime());
        return updateChainWrapper.update();

    }

    /**
     * 根据业务订单号获取支付订单
     *
     * @param orderId
     * @return
     */
    @Override
    public PayOrder getByOrderId(String orderId) {
        PayOrderPageDto payOrderPageDto = new PayOrderPageDto();
        payOrderPageDto.setOrderId(orderId);
        var wrapper = buildQueryWrapper(payOrderPageDto);
        wrapper.last("limit 1");
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 批量删除支付订单
     *
     * @param ids 需要删除的支付订单主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

}
