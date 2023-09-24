package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏配置对象 game_config
 *
 * @author euler
 * @date 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_config")
public class GameConfig extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 游戏Id
     */
    private Integer gameId;

    /**
     * 运行平台 (1 安卓 2 ios 3 h5)
     */
    private String platform;

    /**
     * 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播
     */
    private String type;

    /**
     * 游戏配置数据，格式为json格式
     */
    private String data;

    /**
     * 删除状态(0 正常  1删除)
     */
    @TableLogic
    private String delFlag;

}
