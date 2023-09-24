package com.euler.common.payment.bean;

import java.util.HashMap;

/**
 * @Description: 微信支付V3 通知应答
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_1_5.shtml
 **/
public class WxPayResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private static final String FAIL = "FAIL";
    private static final String SUCCESS = "SUCCESS";

    public static WxPayResult error() {
        WxPayResult r = new WxPayResult();
        r.put("code", FAIL);
        r.put("message", "失败");
        return r;
    }

    public static WxPayResult ok() {
        WxPayResult r = new WxPayResult();
        r.put("code", SUCCESS);
        r.put("message", "成功");
        return r;
    }

    @Override
    public WxPayResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

