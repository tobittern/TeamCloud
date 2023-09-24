package com.euler.sdk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 游戏配置类型
 * 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播
 */
@Getter
@AllArgsConstructor
public enum GameConfigEnum {

    SDK_MENU("1", "SDK菜单"),
    WALLET_MENU("2", "SDK钱包菜单"),
    VIRTUAL_WALLET_MENU("3", "SDK虚拟钱包菜单"),
    GAME_PAY_TYPE("4", "游戏支付方式"),
    APP_IOS_PAYMENT_TERM("5", "苹果应用类支付条件"),
    EVENT_BROADCAST("6", "事件广播"),
    ;
    private String code;
    private String desc;

    public static GameConfigEnum find(String code) {
        for (GameConfigEnum enumd : values()) {
            if (enumd.getCode().equals(code)) {
                return enumd;
            }
        }
        throw new RuntimeException("'GameUseRecordTypeEnum' not found By " + code);
    }
}
