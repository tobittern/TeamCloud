package com.euler.common.payment.core;

import com.euler.common.payment.bean.TradeResult;

public interface CallablePlus<T> {




     T call(TradeResult tradeResult) throws Exception ;

}
