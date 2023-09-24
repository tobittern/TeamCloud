package com.euler.payment.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.factory.PayFactory;
import com.euler.payment.config.WebConfig;
import com.euler.payment.domain.BusinessOrder;
import com.euler.payment.domain.NotifyRecord;
import com.euler.payment.domain.PayOrder;
import com.euler.payment.domain.dto.BusinessCallbackDto;
import com.euler.payment.domain.dto.NotifyRecordPageDto;
import com.euler.payment.api.domain.NotifyRecordVo;
import com.euler.payment.enums.OrderStatusEnum;
import com.euler.payment.mapper.NotifyRecordMapper;
import com.euler.payment.service.IBusinessOrderService;
import com.euler.payment.service.INotifyRecordService;
import com.euler.payment.service.IPayOrderService;
import com.euler.platform.api.RemoteGameManagerService;
import com.euler.platform.api.domain.OpenGameDubboVo;
import com.euler.sdk.api.RemoteGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 通知记录Service业务层处理
 *
 * @author euler
 * @date 2022-03-29
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class NotifyRecordServiceImpl extends ServiceImpl<NotifyRecordMapper, NotifyRecord> implements INotifyRecordService {

    private final NotifyRecordMapper baseMapper;
    @Autowired
    private IBusinessOrderService businessOrderService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private WebConfig webConfig;
    @DubboReference
    private RemoteGoodsService remoteGoodsService;
    @DubboReference
    private RemoteGameManagerService remoteGameManagerService;

    /**
     * 查询通知记录
     *
     * @param id 通知记录主键
     * @return 通知记录
     */
    @Override
    public NotifyRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询通知记录列表
     *
     * @param bo 通知记录
     * @return 通知记录
     */
    @Override
    public TableDataInfo<NotifyRecordVo> queryPageList(NotifyRecordPageDto bo) {
        LambdaQueryWrapper<NotifyRecord> lqw = buildQueryWrapper(bo);
        Page<NotifyRecordVo> result = baseMapper.selectVoPage(bo.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询通知记录列表
     *
     * @param bo 通知记录
     * @return 通知记录
     */
    @Override
    public List<NotifyRecordVo> queryList(NotifyRecordPageDto bo) {
        LambdaQueryWrapper<NotifyRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<NotifyRecord> buildQueryWrapper(NotifyRecordPageDto bo) {
        LambdaQueryWrapper<NotifyRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessOrderId()), NotifyRecord::getBusinessOrderId, bo.getBusinessOrderId());
        return lqw;
    }

    /**
     * 通知业务方，业务方需要返回success，其他值认为是通知失败
     *
     * @param payOrder      支付订单
     * @param businessOrder 业务订单
     * @param businessType  1:支付，2：退款
     */
    @Transactional
    @Override
    public void notifyBusiness(PayOrder payOrder, BusinessOrder businessOrder, Integer businessType) {
        //通知业务方
        log.info("通知业务方--开始，订单id：{}", payOrder.getId());
        NotifyRecord notifyRecord = new NotifyRecord();
        notifyRecord.setId(PayFactory.getTradeNo("N"));

        notifyRecord.setBusinessOrderId(payOrder.getBusinessOrderId());
        notifyRecord.setState(NotifyRecord.NOTIFY_STATE_ING);
        notifyRecord.setOrderType(businessType);

        try {
            if ("P".equals(businessOrder.getOrderType())) {
                //调用微服务
                R r = remoteGoodsService.buyGoodsCallback(Convert.toInt(businessOrder.getGoodsId(), 0), businessOrder.getGameId(), businessOrder.getMemberId());
                log.info("购买成功后下发商品：{}", JsonUtils.toJsonString(r));
                if (Constants.SUCCESS.equals(r.getCode())) {
                    notifyRecord.setState(NotifyRecord.NOTIFY_STATE_SUCCESS);
                } else {
                    notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
                    notifyRecord.setResResult(r.getMsg());
                }
            } else {
                List<Integer> ids = new ArrayList<>();
                ids.add(Convert.toInt(businessOrder.getGameId(), 0));
                List<OpenGameDubboVo> openGameDubboVos = remoteGameManagerService.selectByIds(ids);

                if (openGameDubboVos != null && !openGameDubboVos.isEmpty()) {
                    var gameInfo = openGameDubboVos.get(0);

                    String url = gameInfo.getRechargeCallback();
                    String secret = gameInfo.getCallbackSecretKey();

                    if (StringUtils.isNotEmpty(url)) {
                        //商品id，会员id，订单id
                        BusinessCallbackDto callbackDto = new BusinessCallbackDto();
                        callbackDto.setEventType(businessType==1 ? BusinessCallbackDto.EVENT_PAY : BusinessCallbackDto.EVENT_REFUND)
                            .setNotifyId(notifyRecord.getId())
                            .setOutTradeNo(businessOrder.getOutTradeNo())
                            .setGoodsId(businessOrder.getGoodsId())
                            .setBody(payOrder.getBody())
                            .setSubject(payOrder.getSubject())
                            .setGoodsImg(businessOrder.getGoodsImg())
                            .setGoodsNum(businessOrder.getGoodsNum())
                            .setOrderAmount(businessOrder.getOrderAmount())
                            .setMemberId(businessOrder.getMemberId())
                            .setStatus(OrderStatusEnum.TRADE_SUCCESS.getCode())
                            .setPayOrderId(payOrder.getBusinessOrderId())
                            .setExtData(businessOrder.getExtData());
                        String rasStr = "";
                        String jsonData = JsonUtils.toJsonString(callbackDto);
                        if (StringUtils.isNotEmpty(secret)) {
                            try {
                                RSA rsa = new RSA(null, secret);
                                rasStr = rsa.encryptBase64(jsonData, KeyType.PublicKey);
                            } catch (Exception e) {
                                log.error("通知业务方--秘钥加密错误,不通知,orderId:{}", businessOrder.getId(), e);
                                notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
                                notifyRecord.setResResult("秘钥加密错误," + e.getMessage());
                                return;
                            }
                        }

                        String res = "";
                        String postData = JsonUtils.toJsonString(R.ok("", rasStr));
                        try {
                            res = HttpUtil.post(url, postData, webConfig.getNotifyGameTimeout());
                        } catch (Exception e) {
                            log.error("通知业务方--调用业务方通知地址异常,orderId:{},outTradeNo:{},jsonData:{},postData{},res:{}", businessOrder.getId(), businessOrder.getOutTradeNo(), jsonData, postData, res, e);
                            notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
                            notifyRecord.setResResult("调用业务方通知地址异常," + e.getMessage());
                            return;
                        }

                        notifyRecord.setResResult(res);

                        if ("success".equalsIgnoreCase(res)) {
                            notifyRecord.setState(NotifyRecord.NOTIFY_STATE_SUCCESS);
                            log.info("通知业务方--通知成功,orderId:{},outTradeNo:{},jsonData:{},postData{},res:{}", businessOrder.getId(), businessOrder.getOutTradeNo(), jsonData, postData, res);
                        } else {
                            notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
                            log.info("通知业务方--通知失败，业务方返回失败,orderId:{},outTradeNo:{},jsonData:{},postData{},res:{}", businessOrder.getId(), businessOrder.getOutTradeNo(), jsonData, postData, res);
                        }
                    } else {
                        log.info("通知业务方--游戏未设置回调地址，不通知，orderId:{}，outTradeNo:{}，gameId：{}", businessOrder.getId(), businessOrder.getOutTradeNo(), businessOrder.getGameId());
                        notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
                        notifyRecord.setResResult("通知地址为空，不通知");
                    }
                } else {
                    log.info("通知业务方--根据游戏id获取游戏信息为空，不通知，orderId:{}，outTradeNo:{}，gameId：{}", businessOrder.getId(), businessOrder.getOutTradeNo(), businessOrder.getGameId());
                    notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
                    notifyRecord.setResResult("通知地址为空，不通知");
                }
            }
        } catch (Exception e) {
            notifyRecord.setState(NotifyRecord.NOTIFY_STATE_FAILED);
            notifyRecord.setResResult("通知业务方异常," + e.getMessage());
            log.error("通知业务方异常：{}", JsonUtils.toJsonString(payOrder), e);
        } finally {
            this.save(notifyRecord);
            payOrder.setNotifyCount(Convert.toInt(payOrder.getNotifyCount(), 0) + 1);
            payOrder.setLastNotifyTime(new Date());

            //判断订单通知状态
            if (!NotifyRecord.NOTIFY_STATE_SUCCESS.equals(businessOrder.getNotifyState())) {
                businessOrder.setNotifyState(notifyRecord.getState());
                payOrder.setNotifyState(notifyRecord.getState());
            }
            log.info("通知业务方--结束，订单id：{}", payOrder.getId());
        }
    }

}
