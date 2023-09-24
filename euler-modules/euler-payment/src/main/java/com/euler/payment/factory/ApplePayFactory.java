package com.euler.payment.factory;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.euler.common.core.utils.SpringUtils;
import com.euler.common.payment.bean.Trade;
import com.euler.common.payment.core.Pay;
import com.euler.payment.enums.ApplePayTypeEnumd;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ApplePayFactory {

    private static final AtomicLong PAY_ORDER_SEQ = new AtomicLong(0L);

    public static Pay<Trade> getPayType(String type) {
        ApplePayTypeEnumd enumd = ApplePayTypeEnumd.find(type);
        return (Pay) SpringUtils.getBean(enumd.getBeanClass());
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
