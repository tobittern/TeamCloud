package com.euler.payment.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.lock.LockInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.constant.UserConstants;
import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.DeviceInfoDto;
import com.euler.common.core.exception.ServiceException;
import com.euler.common.core.utils.*;
import com.euler.common.mybatis.core.page.TableDataInfo;
import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.bean.TradeResult;
import com.euler.common.payment.bean.TradeToken;
import com.euler.common.payment.config.PayMqConfig;
import com.euler.common.payment.core.*;
import com.euler.common.payment.enums.PayTypeEnumd;
import com.euler.common.payment.factory.PayFactory;
import com.euler.common.payment.utils.PayUtils;
import com.euler.common.rabbitmq.RabbitMqHelper;
import com.euler.common.redis.utils.LockHelper;
import com.euler.common.satoken.utils.LoginHelper;
import com.euler.payment.api.domain.BusinessOrderVo;
import com.euler.payment.api.domain.MemberOrderStatDto;
import com.euler.payment.bean.AppleTradeResult;
import com.euler.payment.config.WebConfig;
import com.euler.payment.domain.*;
import com.euler.payment.domain.dto.*;
import com.euler.payment.domain.vo.StatisticsChargeVo;
import com.euler.payment.enums.ApplePayTypeEnumd;
import com.euler.payment.enums.PayChannelEnum;
import com.euler.payment.factory.ApplePayFactory;
import com.euler.payment.mapper.BusinessOrderMapper;
import com.euler.payment.service.IBusinessOrderService;
import com.euler.payment.service.INotifyRecordService;
import com.euler.payment.service.IOrderLogService;
import com.euler.payment.service.IPayOrderService;
import com.euler.payment.utils.AppleVerifyUtil;
import com.euler.payment.utils.PayChannelUtils;
import com.euler.sdk.api.RemoteGoodsService;
import com.euler.sdk.api.RemoteMemberService;
import com.euler.sdk.api.domain.GoodsVo;
import com.euler.sdk.api.domain.LoginMemberVo;
import com.euler.sdk.api.domain.WalletVo;
import com.euler.sdk.api.enums.RechargeTypeEnum;
import com.euler.common.core.domain.dto.LoginUser;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 订单Service业务层处理
 *
 * @author euler
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessOrderServiceImpl extends ServiceImpl<BusinessOrderMapper, BusinessOrder> implements IBusinessOrderService {

    //region 初始化
    private final BusinessOrderMapper baseMapper;
    @Autowired
    private PayChannelUtils payChannelUtils;
    @Autowired
    private WebConfig webConfig;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IOrderLogService orderLogService;
    @Autowired
    private AliPay aliPay;
    @Autowired
    private WxPay wxPay;
    @Autowired
    private PayNotifySevice payNotifySevice;
    @Autowired
    private INotifyRecordService notifyRecordService;
    @Autowired
    private LockHelper lockHelper;
    @Autowired
    private AppleVerifyUtil appleVerifyUtil;
    @Autowired
    private PayMqConfig payMqConfig;
    @Autowired
    private RabbitMqHelper rabbitMqHelper;
    @Autowired
    private PayUtils payUtils;
    @DubboReference
    private RemoteMemberService remoteMemberService;
    @DubboReference
    private RemoteGoodsService remoteGoodsService;
    @DubboReference
    private RemoteDictService remoteDictService;
    //endregion 初始化

    //region 后台方法

    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    @Override
    public BusinessOrderVo queryById(String id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询订单列表
     *
     * @param businessOrderPageDto 订单
     * @return 订单
     */
    @Override
    public TableDataInfo<BusinessOrderVo> queryPageList(BusinessOrderPageDto businessOrderPageDto) {
        LambdaQueryWrapper<BusinessOrder> lqw = buildQueryWrapper(businessOrderPageDto);
        Page<BusinessOrderVo> result = baseMapper.selectVoPage(businessOrderPageDto.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<BusinessOrderVo> queryList(BusinessOrderPageDto businessOrderPageDto) {
        LambdaQueryWrapper<BusinessOrder> lqw = buildQueryWrapper(businessOrderPageDto);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BusinessOrder> buildQueryWrapper(BusinessOrderPageDto bo) {
        LambdaQueryWrapper<BusinessOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPayOrderState() != null, BusinessOrder::getPayOrderState, bo.getPayOrderState());
        lqw.eq(bo.getOrderState() != null, BusinessOrder::getOrderState, bo.getOrderState());
        lqw.eq(bo.getMemberId() != null, BusinessOrder::getMemberId, bo.getMemberId());
        lqw.eq(StringUtils.isNotBlank(bo.getMemberNickName()), BusinessOrder::getMemberNickName, bo.getMemberNickName());
        lqw.eq(StringUtils.isNotBlank(bo.getMemberMobile()), BusinessOrder::getMemberMobile, bo.getMemberMobile());
        lqw.eq(StringUtils.isNotBlank(bo.getOutTradeNo()), BusinessOrder::getOutTradeNo, bo.getOutTradeNo());


        lqw.eq(bo.getRefundOrderState() != null, BusinessOrder::getRefundOrderState, bo.getRefundOrderState());
        lqw.eq(bo.getNotifyState() != null, BusinessOrder::getNotifyState, bo.getNotifyState());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessOrderId()), BusinessOrder::getId, bo.getBusinessOrderId());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderType()), BusinessOrder::getOrderType, bo.getOrderType());
        lqw.eq(StringUtils.isNotBlank(bo.getPayChannel()), BusinessOrder::getPayChannel, bo.getPayChannel());
        lqw.eq(StringUtils.isNotBlank(bo.getGamePackageCode()), BusinessOrder::getGamePackageCode, bo.getGamePackageCode());
        lqw.eq(StringUtils.isNotBlank(bo.getGameName()), BusinessOrder::getGameName, bo.getGameName());
        lqw.eq(StringUtils.isNotBlank(bo.getGameServerName()), BusinessOrder::getGameServerName, bo.getGameServerName());
        lqw.eq(StringUtils.isNotBlank(bo.getGameRoleName()), BusinessOrder::getGameRoleName, bo.getGameRoleName());
        lqw.eq(StringUtils.isNotBlank(bo.getGameChannel()), BusinessOrder::getGameChannelName, bo.getGameChannel());
        lqw.eq(StringUtils.isNotBlank(bo.getGameRoleId()), BusinessOrder::getGameRoleId, bo.getGameRoleId());
        lqw.ge(StringUtils.isNotBlank(bo.getBeginTime()), BusinessOrder::getCreateTime, DateUtils.getBeginOfDay(bo.getBeginTime()));
        lqw.le(StringUtils.isNotBlank(bo.getEndTime()), BusinessOrder::getCreateTime, DateUtils.getEndOfDay(bo.getEndTime()));

        lqw.ge(StringUtils.isNotBlank(bo.getPayBeginTime()), BusinessOrder::getSuccessTime, DateUtils.getBeginOfDay(bo.getPayBeginTime()));
        lqw.le(StringUtils.isNotBlank(bo.getPayEndTime()), BusinessOrder::getSuccessTime, DateUtils.getEndOfDay(bo.getPayEndTime()));

        lqw.likeRight(StringUtils.isNotBlank(bo.getGoodsName()), BusinessOrder::getGoodsName, bo.getGoodsName());
        lqw.eq((bo.getChannelId() != null && bo.getChannelId() != 0), BusinessOrder::getGameChannelId, bo.getChannelId());

        return lqw;
    }

    /**
     * 补单操作
     *
     * @param orderId 订单Id
     * @param opUser 补单人
     * @param opContent 补单的内容描述
     * @param isAuto 是否是自动补单
     */
    @Transactional
    @Override
    public R supplyOrder(String orderId, String opUser, String opContent, boolean isAuto) {
        String msg;
        var businessOrder = getById(orderId);
        if (businessOrder == null) {
            msg = StringUtils.format("自动补单操作--订单不存在，订单号：{},支付渠道单号：{}", orderId);
            log.info(msg);
            return R.fail(msg);
        }

        var payOrder = payOrderService.getById(businessOrder.getOrderType() + orderId);
        if (payOrder == null) {
            msg = StringUtils.format("自动补单操作--支付订单不存在，订单号：{},支付渠道单号：{}", orderId);
            log.info(msg);
            return R.fail(msg);
        }

        if (isAuto && payOrder.getNotifyCount() >= payOrder.getNotifyCountLimit())
            return R.fail("已达到最大补单次数");

        if (BusinessOrder.ORDER_STATE_INIT.equals(businessOrder.getOrderState())) {
            msg = StringUtils.format("自动补单操作--待支付订单，不允许补单");
            log.info(msg);
            return R.fail(msg);
        }

        if (BusinessOrder.ORDER_STATE_SUCCESS.equals(businessOrder.getOrderState())) {
            msg = StringUtils.format("自动补单操作--已完成订单，不允许补单");
            log.info(msg);
            return R.fail(msg);
        }

        if (BusinessOrder.ORDER_STATE_CLOSE.equals(businessOrder.getOrderState())) {
            msg = StringUtils.format("自动补单操作--已关闭订单，不允许补单");
            log.info(msg);
            return R.fail(msg);
        }

        if (NotifyRecord.NOTIFY_STATE_SUCCESS.equals(payOrder.getNotifyState())) {
            msg = StringUtils.format("自动补单操作--已经下发过此订单，不允许再次下发");
            log.info(msg);
            return R.fail(msg);
        }

        //添加通知信息
        notifyRecordService.notifyBusiness(payOrder, businessOrder, 1);
        //更新订单通知信息
        payOrderService.updatePayOrderInfo(payOrder);

        updateOrderInfo(businessOrder);

        String notifyMsg = NotifyRecord.NOTIFY_STATE_SUCCESS.equals(payOrder.getNotifyState()) ? "，成功" : "，失败";
        //记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setBusinessOrderId(orderId);
        orderLog.setOrderType(OrderLog.ORDERTYPE_PAY);
        orderLog.setCreateBy(opUser);
        orderLog.setOpContent(opContent + notifyMsg);
        orderLogService.save(orderLog);

        if (NotifyRecord.NOTIFY_STATE_SUCCESS.equals(payOrder.getNotifyState()))
            return R.ok();
        else
            return R.fail("通知游戏方失败");
    }
    //endregion 后台方法

    //region 统一下单
    /**
     * 统一下单
     *
     * @param dto 统一下单Dto
     * @return 统一下单结果
     */
    @Override
    public TradeToken<String> unifiedOrder(UnifiedOrderDto dto) {
        log.info("统一下单--前端提交下单参数：{}", JsonUtils.toJsonString(dto));
        PayChannelDto payChannelDto = payChannelUtils.convertPayChannel(dto.getPayChannelCode());
        String orderId;
        if(StringUtils.equals("apple_iap", dto.getPayChannelCode())) {
            orderId = ApplePayFactory.getTradeNo();
        } else {
            orderId = PayFactory.getTradeNo();
        }
        BusinessOrder businessOrder = genOrder(orderId, dto, payChannelDto);

        //4、调用支付渠道支付
        Trade trade = new Trade();
        trade.setPayType(payChannelDto.getPayType());
        trade.setOutTradeNo(dto.getOrderType() + orderId);
        trade.setBody(businessOrder.getGoodsName());
        trade.setSubject(businessOrder.getGoodsName());
        trade.setTotalAmount(businessOrder.getOrderAmount().toString());
        trade.setBusinessOrderId(orderId);
        log.info("统一下单--提交给支付渠道参数：{}", JsonUtils.toJsonString(trade));

        // 支付宝、微信、钱包、苹果内购
        // 判断是否是苹果内购
        TradeToken<String> tradeToken;
        if(StringUtils.equals("apple_iap", dto.getPayChannelCode())) {
            var sss = ApplePayFactory.getPayType(dto.getPayChannelCode());
            tradeToken = sss.pay(trade);
        } else {
            var sss = PayFactory.getPayType(dto.getPayChannelCode());
            tradeToken = sss.pay(trade);
        }

        log.info("统一下单--支付下单结果：{}", JsonUtils.toJsonString(tradeToken.value()));
        //钱包支付，扣除钱包余额，直接订单成功
        if (PayChannelEnum.WALLET.getPayChannel().equals(payChannelDto.getPayChannel())) {
            PayTypeEnumd payTypeEnumd = PayTypeEnumd.find(dto.getPayChannelCode());
            TradeResult tradeResult = new TradeResult(payTypeEnumd);
            tradeResult.setSuccess(true);
            tradeResult.setBusinessType(1);
            tradeResult.setOutTradeNo(trade.getOutTradeNo());
            tradeResult.setTradeStatus("TRADE_SUCCESS");
            try {
                payNotifySevice.payStatus(tradeResult);
            } catch (Exception e) {
                log.error("统一下单--钱包支付--成功后回调异常:{}", JsonHelper.toJson(tradeResult), e);
            }

            // 校验游戏配置里是否配置了事件广播
            if(payUtils.checkIsOpenBroadcast()) {
                var headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
                DeviceInfoDto deviceInfo = headerDto.getDeviceInfo();
                String msgId = "wallet_pay_" + trade.getOutTradeNo();
                Map<String, Object> map = new HashMap<>();
                map.put("member_id", businessOrder.getMemberId());
                map.put("good_id", trade.getOutTradeNo());
                map.put("good_name", businessOrder.getGoodsName());
                map.put("price", businessOrder.getOrderAmount());
                map.put("device_info", deviceInfo);
                map.put("status", "success");
                // 支付成功的订单，在消息队列里发送一条成功的消息
                rabbitMqHelper.sendObj(payMqConfig.getPayExchange(), payMqConfig.getPayRoutingKey(), msgId, map);
            }
        }

        return tradeToken;
    }


    @Transactional(rollbackFor = Exception.class)
    public BusinessOrder genOrder(String orderId, UnifiedOrderDto dto, PayChannelDto payChannelDto) {
        LoginUser loginUser = LoginHelper.getLoginUser();

        //1、创建订单
        BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setId(orderId);
        businessOrder.setOrderType(dto.getOrderType());
        businessOrder.setPayChannel(payChannelDto.getPayChannelCode());
        businessOrder.setPayOrderState(PayOrder.STATE_ING);//初始化支付状态
        businessOrder.setOrderState(BusinessOrder.ORDER_STATE_INIT);//初始化订单状态
        businessOrder.setRefundOrderState(RefundOrder.STATE_NONE);//初始化退款状态
        businessOrder.setExpiredTime(DateUtil.offsetMinute(new Date(), webConfig.getOrderExpiredTime()));

        //设置订单会员信息
        LoginMemberVo loginMemberVo = remoteMemberService.getMemberById(LoginHelper.getUserId());
        businessOrder.setMemberId(loginMemberVo.getId());
        businessOrder.setMemberAccount(loginMemberVo.getAccount());
        businessOrder.setMemberMobile(loginMemberVo.getMobile());
        businessOrder.setMemberNickName(loginMemberVo.getNickName());

        //设置商品信息
        businessOrder.setGoodsId(dto.getGoodsId());
        val sdkChannelPackageDto = loginUser.getSdkChannelPackage();
        if (dto.getOrderType().equals("P")) {
            GoodsVo goodsVo = remoteGoodsService.orderQueryById(Convert.toInt(dto.getGoodsId()), LoginHelper.getUserId());
            businessOrder.setGoodsNum(1);
            businessOrder.setGoodsDesc(goodsVo.getGoodsDesc());
            businessOrder.setGoodsImg(goodsVo.getGoodsIcon());
            businessOrder.setGoodsName(goodsVo.getGoodsName());
            businessOrder.setGoodsPrice(goodsVo.getGoodsPrice());
            businessOrder.setGoodsScribePrice(goodsVo.getGoodsScribePrice());
            businessOrder.setOrderAmount(goodsVo.getGoodsPrice());
        } else {
            if (sdkChannelPackageDto == null || sdkChannelPackageDto.getGameRoleId() == null)
                throw new ServiceException("游戏角色信息未上传");

            businessOrder.setGoodsNum(dto.getGoodsNum());
            businessOrder.setOutTradeNo(dto.getOutTradeNo());
            businessOrder.setGoodsDesc(dto.getBody());
            businessOrder.setGoodsImg(dto.getGoodsImg());
            businessOrder.setGoodsName(dto.getSubject());
            businessOrder.setGoodsPrice(dto.getOrderAmount());
            businessOrder.setGoodsScribePrice(dto.getOrderAmount());
            businessOrder.setOrderAmount(dto.getOrderAmount());
            businessOrder.setExtData(dto.getExtData());
        }
        //设置游戏信息
        if (sdkChannelPackageDto != null) {
            businessOrder.setGameId(sdkChannelPackageDto.getGameId());
            businessOrder.setGameChannelId(sdkChannelPackageDto.getChannelId());
            businessOrder.setGameChannelName(sdkChannelPackageDto.getChannelName());
            businessOrder.setGameName(sdkChannelPackageDto.getGameName());
            businessOrder.setGamePackageCode(sdkChannelPackageDto.getPackageCode());
            businessOrder.setGameRoleId(sdkChannelPackageDto.getGameRoleId());
            businessOrder.setGameRoleName(sdkChannelPackageDto.getGameRoleName());
            businessOrder.setGameServerId(sdkChannelPackageDto.getGameServerId());
            businessOrder.setGameServerName(sdkChannelPackageDto.getGameServerName());
        }

        if(!StringUtils.equals("apple_iap", dto.getPayChannelCode())) {
            //校验余额
            PayTypeEnumd payTypeEnumd = PayTypeEnumd.find(dto.getPayChannelCode());

            if (PayChannelEnum.WALLET.getPayChannel().equals(payChannelDto.getPayChannel())) {
                WalletVo wallet = remoteMemberService.getMyWallet(loginUser.getUserId());
                if (wallet == null || !UserConstants.NORMAL.equals(wallet.getStatus())) {
                    throw new ServiceException("钱包已停用");
                }
                RechargeTypeEnum rechargeTypeEnum = null;
                //平台币校验
                if (PayTypeEnumd.WalletPlatform.equals(payTypeEnumd)) {
                    if (Convert.toBigDecimal(wallet.getPlatformCurrency().toString()).compareTo(businessOrder.getOrderAmount()) < 0)
                        throw new ServiceException("钱包平台币不足");
                    rechargeTypeEnum = RechargeTypeEnum.platform_currency;

                }
                if (PayTypeEnumd.WalletBalance.equals(payTypeEnumd)) {
                    if (Convert.toBigDecimal(wallet.getBalance().toString()).compareTo(businessOrder.getOrderAmount()) < 0)
                        throw new ServiceException("钱包余额不足");
                    rechargeTypeEnum = RechargeTypeEnum.balance;

                }
                remoteMemberService.modifyWallet(loginUser.getUserId(), businessOrder.getGameId(), 1, businessOrder.getOrderAmount().negate(), rechargeTypeEnum, 2, "钱包消费");
            }
        }
        this.save(businessOrder);

        //2、创建支付订单
        PayOrder payOrder = new PayOrder();
        payOrder.setId(dto.getOrderType() + orderId);
        payOrder.setBusinessOrderId(orderId);
        payOrder.setAmount(businessOrder.getOrderAmount());
        payOrder.setState(PayOrder.STATE_ING);
        payOrder.setClientIp(ServletUtils.getClientIP());
        payOrder.setSubject(businessOrder.getGoodsName());
        payOrder.setBody(businessOrder.getGoodsDesc());
        payOrder.setPayChannel(dto.getPayChannelCode());
        payOrder.setExpiredTime(businessOrder.getExpiredTime());
        // payOrder.setNotifyUrl("appid的异步通知地址");
        payOrderService.save(payOrder);

        //3、记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setBusinessOrderId(orderId);
        orderLog.setOrderType(OrderLog.ORDERTYPE_PAY);
        orderLog.setOpContent("用户发起订单");
        orderLog.setCreateBy(LoginHelper.getUsername());

        orderLogService.save(orderLog);
        return businessOrder;
    }
    //endregion 统一下单

    //region 查询支付订单状态
    /**
     * 查询支付订单状态
     *
     * @param outTradeNo 外部订单号
     * @return 支付订单状态
     */
    @Override
    public TradeResult queryPayOrderState(String outTradeNo) {
        var businessOrder = getById(outTradeNo);
        if (businessOrder == null)
            throw new ServiceException("系统订单不存在");
        //获取订单详情，以便获取支付渠道
        PayOrder payOrder = payOrderService.getById(businessOrder.getOrderType() + outTradeNo);
        if (payOrder == null)
            throw new ServiceException("系统订单不存在");

        Trade trade = new Trade();
        trade.setOutTradeNo(payOrder.getId());
        TradeResult result;
        // 判断是否是苹果内购
        if(StringUtils.equals("apple_iap", payOrder.getPayChannel())) {
            var tradePay = ApplePayFactory.getPayType(payOrder.getPayChannel());
            result = tradePay.query(trade);
            payNotifySevice.payStatus(result);
        } else {
            var tradePay = PayFactory.getPayType(payOrder.getPayChannel());
            result = tradePay.query(trade);
            payNotifySevice.payStatus(result);
        }
        return result;
    }

    @Override
    public FrontQueryResultDto frontQueryOrderState(String businessOrderId) {
        String lockKey = StringUtils.format("{}lock:orderstate:{}", Constants.BASE_KEY, businessOrderId);
        LockInfo lockInfo = lockHelper.lock(lockKey, 3, 2000L);
        try {
            FrontQueryResultDto frontQueryResultDto = new FrontQueryResultDto();
            frontQueryResultDto.setBusinessOrderId(businessOrderId);
            var businessOrder = getById(businessOrderId);
            if (businessOrder == null)
                throw new ServiceException("系统订单不存在");
            if (null == lockInfo) {
                frontQueryResultDto.setOrderState(businessOrder.getOrderState());
                return frontQueryResultDto;
            }
            lockHelper.unLock(lockInfo);

            if (BusinessOrder.ORDER_STATE_INIT.equals(businessOrder.getOrderState())) {
                PayOrder payOrder = payOrderService.getById(businessOrder.getOrderType() + businessOrderId);
                if (payOrder == null)
                    throw new ServiceException("系统订单不存在");

                Trade trade = new Trade();
                trade.setOutTradeNo(payOrder.getId());
                TradeResult result;
                // 判断是否是苹果内购
                if(StringUtils.equals("apple_iap", payOrder.getPayChannel())) {
                    var tradePay = ApplePayFactory.getPayType(payOrder.getPayChannel());
                    result = tradePay.query(trade);
                } else {
                    var tradePay = PayFactory.getPayType(payOrder.getPayChannel());
                    result = tradePay.query(trade);
                }
                payNotifySevice.payStatus(result);
            }
            businessOrder = getById(businessOrderId);
            frontQueryResultDto.setOrderState(businessOrder.getOrderState());
            return frontQueryResultDto;
        } finally {
            //释放锁
            lockHelper.unLock(lockInfo);
        }
    }
    //endregion

    //region 异步回调
    /**
     * 支付宝异步回调
     */
    @Override
    public void orderAliNotify(HttpServletRequest request, HttpServletResponse response) {

        CallablePlus c = tradeResult -> {
            //处理业务
            payNotifySevice.payStatus(tradeResult);
            return "";
        };

        aliPay.AsyncNotify(request, response, c);
    }

    /**
     * 微信异步回调
     */
    @Override
    public void orderWxNotify(HttpServletRequest request, HttpServletResponse response) {

        //解析请求参数
        CallablePlus c = tradeResult -> {
            //处理业务
            payNotifySevice.payStatus(tradeResult);
            return "";
        };

        wxPay.AsyncNotify(request, response, c);
    }

    /**
     * 苹果内购回调
     *
     * @param dto 苹果内购Dto
     */
    @Override
    public R orderApplePay(ApplePayDto dto) {
        // 请求苹果服务器进行票据验证
        String verifyResult = appleVerifyUtil.buyAppVerify(dto.getReceipt(), 1);
        // 解析票据
        if (verifyResult == null) {
            return R.fail("无订单信息");
        } else {
            JSONObject receiptData = JSONObject.parseObject(verifyResult);
            // 支付环境是否正确
            String status = receiptData.getString("status");

            AtomicBoolean isSandBox = new AtomicBoolean(false);
            // 查询字典，获取苹果内购沙盒验证
            List<SysDictData> data = remoteDictService.selectDictDataByType("apple_iap_sandbox_check");
            if (data != null && !data.isEmpty()) {
                data.forEach(a -> {
                    // 判断沙盒开关是否开启，0正常（沙盒），1停用（线上）
                    if (a.getDictLabel().equals("sandbox_switch") && StringUtils.equals("0", a.getStatus())) {
                        isSandBox.set(true);
                    }
                });
            }

            if ("21007".equals(status) && isSandBox.get()) {
                // 验证失败21007 走沙箱环境
                verifyResult = appleVerifyUtil.buyAppVerify(dto.getReceipt(), 0);
                receiptData = JSONObject.parseObject(verifyResult);
                status = receiptData.getString("status");
            }

            // 处理订单
            if ("0".equals(status)) {
                JSONObject receiptInfo = receiptData.getJSONObject("receipt");
                JSONArray inAppList = receiptInfo.getJSONArray("in_app");
                if (!CollectionUtils.isEmpty(inAppList)) {
                    JSONObject inApp = inAppList.getJSONObject(inAppList.size() - 1);
                    // 订单号
                    String transactionId = inApp.getString("transaction_id");
                    // 商品ID 与在APP Store 后台配置的一致
                    String productId = inApp.getString("product_id");
                    log.info("支付---> transactionId:{}, productId:{}", transactionId, productId);
                    BusinessOrderVo vo = baseMapper.selectVoById(dto.getTransactionId());
                    Map retMap = new HashMap<>();
                    if(vo != null) {
                        ApplePayTypeEnumd payTypeEnumd = ApplePayTypeEnumd.find("apple_iap");
                        AppleTradeResult tradeResult = new AppleTradeResult(payTypeEnumd);
                        tradeResult.setSuccess(true);
                        tradeResult.setBusinessType(1);
                        tradeResult.setOutTradeNo(vo.getOrderType() + dto.getTransactionId());
                        tradeResult.setTradeStatus("TRADE_SUCCESS");

                        TradeResult newTradeResult = BeanCopyUtils.copy(tradeResult, TradeResult.class);
                        try {
                            // 修改订单状态
                            payNotifySevice.payStatus(newTradeResult);

                            // 设置苹果的外部订单号
                            var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper)
                                .eq(BusinessOrder::getId, dto.getTransactionId())
                                .set(BusinessOrder::getAppleTradeNo, transactionId);
                            updateChainWrapper.update();
                        } catch (Exception e) {
                            log.error("统一下单--苹果内购支付--成功后回调异常:{}", JsonHelper.toJson(tradeResult), e);
                        }
                        retMap.put("goodsId", vo.getGoodsId());
                        retMap.put("goodsName", vo.getGoodsName());
                        retMap.put("orderAmount", vo.getOrderAmount());
                        // 商品标题，订单类型平台消费时为空
                        retMap.put("subject", vo.getGoodsName());
                        // 商品描述信息，订单类型平台消费时为空
                        retMap.put("body", vo.getGoodsDesc());
                        return R.ok(retMap);
                    }
                    return R.fail("订单不存在");
                } else {
                    return R.fail("订单凭证有误");
                }
            }
            return R.fail("无效的订单");
        }
    }
    //endregion 异步回调

    //region 修改订单状态
    /**
     * 修改订单状态
     *
     * @param order 订单信息
     * @return 修改结果
     */
    public boolean updateOrderInfo(BusinessOrder order) {
        var updateChainWrapper = new LambdaUpdateChainWrapper<>(baseMapper);
        updateChainWrapper.eq(BusinessOrder::getId, order.getId())
            .set(order.getOrderState() != null, BusinessOrder::getOrderState, order.getOrderState())
            .set(order.getPayOrderState() != null, BusinessOrder::getPayOrderState, order.getPayOrderState())
            .set(order.getRefundOrderState() != null, BusinessOrder::getRefundOrderState, order.getRefundOrderState())
            .set(order.getSuccessTime() != null, BusinessOrder::getSuccessTime, order.getSuccessTime())
            .set(order.getNotifyState() != null, BusinessOrder::getNotifyState, order.getNotifyState());
        return updateChainWrapper.update();
    }
    //endregion 修改订单状态

    /**
     * 获取会员订单统计数据
     *
     * @param memberId 用户Id
     * @param gameId 游戏Id
     * @return 会员订单统计数据
     */
    @Override
    public MemberOrderStatDto getMemberOrderStatInfo(Long memberId, Integer gameId, BigDecimal rechargeAmount) {
        var lqw = Wrappers.<BusinessOrder>query();
        lqw.lambda().eq(BusinessOrder::getMemberId, memberId)
            .eq(gameId != null && gameId > 0, BusinessOrder::getGameId, gameId)
            .in(BusinessOrder::getOrderState, BusinessOrder.ORDER_STATE_SUCCESS, BusinessOrder.ORDER_STATE_PAY)
            .in(BusinessOrder::getPayOrderState, PayOrder.STATE_FINISH, PayOrder.STATE_SUCCESS)
            .eq(BusinessOrder::getRefundOrderState, RefundOrder.STATE_NONE);
        lqw.select("count(order_amount) as rechargeNum", "sum(order_amount) as rechargeAmount");

        var res = baseMapper.selectMaps(lqw);
        MemberOrderStatDto memberOrderStatDto = new MemberOrderStatDto();
        if (res != null && !res.isEmpty()) {
            List<MemberOrderStatDto> list = JsonUtils.parseArray(JsonUtils.toJsonString(res), MemberOrderStatDto.class);
            if (list != null && !list.isEmpty()) {
                memberOrderStatDto = list.get(0);
            }
        }
        var lqw2 = Wrappers.<BusinessOrder>lambdaQuery();
        lqw2.eq(BusinessOrder::getMemberId, memberId)
            .eq(gameId != null && gameId > 0, BusinessOrder::getGameId, gameId)
            .in(BusinessOrder::getOrderState, BusinessOrder.ORDER_STATE_SUCCESS, BusinessOrder.ORDER_STATE_PAY)
            .in(BusinessOrder::getPayOrderState, PayOrder.STATE_FINISH, PayOrder.STATE_SUCCESS)
            .eq(BusinessOrder::getRefundOrderState, RefundOrder.STATE_NONE)
            .ge(BusinessOrder::getOrderAmount, rechargeAmount);

        boolean exists = baseMapper.exists(lqw2);
        memberOrderStatDto.setCheckRechargeAmount(exists);
        return memberOrderStatDto;
    }

    @Override
    public List<StatisticsChargeVo> getOrderDataByparam(Map<String, Object> map) {
        return baseMapper.getOrderDataByparam(map);
    }

    @Override
    public List<BusinessOrderVo> getOrderChargeByparam(Map<String, Object> map) {
        return baseMapper.getOrderChargeByparam(map);
    }

    /**
     * 查询用户已支付的订单数
     */
    public int getOrderCountByUser(Long userId, Long gameId) {
        return baseMapper.getOrderCountByUser(userId, gameId);
    }

}
