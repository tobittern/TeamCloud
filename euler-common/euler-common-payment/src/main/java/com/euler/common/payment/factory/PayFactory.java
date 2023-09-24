package com.euler.common.payment.factory;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.utils.SpringUtils;
import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.core.IAliComplaintService;
import com.euler.common.payment.core.IWxComplaintService;
import com.euler.common.payment.core.Pay;
import com.euler.common.payment.enums.PayTypeEnumd;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class PayFactory {

    private static final AtomicLong PAY_ORDER_SEQ = new AtomicLong(0L);

    public static Pay<Trade> getPayType(String type) {
        PayTypeEnumd enumd = PayTypeEnumd.find(type);
        return (Pay) SpringUtils.getBean(enumd.getBeanClass());
    }

    public static IWxComplaintService getComplaintServiceType(String type) {
        PayTypeEnumd enumd = PayTypeEnumd.find(type);
        return (IWxComplaintService) SpringUtils.getBean(enumd.getBeanClass());
    }
    public static IAliComplaintService getAliComplaintServiceType(String type) {
        PayTypeEnumd enumd = PayTypeEnumd.find(type);
        return (IAliComplaintService) SpringUtils.getBean(enumd.getBeanClass());
    }

    public static String getTradeNo(String tradeType) {
        return String.format("%s%s%04d", tradeType,
            DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
            (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);

    }

    public static String getTradeNo() {
        return String.format("%s%04d",
            DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
            (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);

    }


}
