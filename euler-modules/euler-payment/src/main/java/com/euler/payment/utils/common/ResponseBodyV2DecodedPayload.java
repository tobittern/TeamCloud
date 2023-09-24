package com.euler.payment.utils.common;

import com.euler.payment.utils.enums.NotificationType;
import com.euler.payment.utils.enums.Subtype;

@lombok.Data
public class ResponseBodyV2DecodedPayload {
    /**
     * 通知类型
     */
    private NotificationType notificationType;

    /**
     * 子类型
     */
    private Subtype subtype;

    /**
     * 数据，包含元数据以及已签名的续订和交易信息的对象
     */
    private Data data;

    /**
     * 通知的唯一标识符
     */
    private String notificationUUID;

    /**
     * 服务器通知的版本号，如"2.0"
     */
    private String version;

}
