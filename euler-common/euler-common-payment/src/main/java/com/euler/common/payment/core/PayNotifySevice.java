package com.euler.common.payment.core;

import com.euler.common.payment.bean.TradeResult;

public interface PayNotifySevice {
    void payStatus(TradeResult tradeResult);
}
