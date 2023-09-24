package com.euler.sdk.domain.bo;
import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import com.euler.sdk.api.domain.GameConfigData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.util.List;

/**
 * 游戏配置业务对象 game_config
 *
 * @author euler
 * @date 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("游戏配置业务对象")
public class GameConfigBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 游戏Id
     */
    @ApiModelProperty(value = "游戏Id")
    private Integer gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    @NotBlank(message = "游戏名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gameName;

    /**
     * 运行平台 (1 安卓 2 ios 3 h5)
     */
    @ApiModelProperty(value = "运行平台 (1 安卓 2 ios 3 h5)")
    private String platform = "1";

    /**
     * 游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播
     */
    @ApiModelProperty(value = "游戏配置类型 1:SDK菜单 2:SDK钱包菜单 3:SDK虚拟钱包菜单 4:游戏支付方式 5:苹果应用类支付条件 6:事件广播", required = true)
    @NotBlank(message = "游戏配置类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 游戏配置数据，格式为json格式
     */
    @ApiModelProperty(value = "游戏配置数据，格式为json格式", required = true)
    @NotBlank(message = "游戏配置数据不能为空", groups = { AddGroup.class, EditGroup.class })
    private String data;

    /**
     * SDK菜单列表(前端页面展示用)
     */
    @ApiModelProperty("SDK菜单列表")
    private List<GameConfigData> sdkMenuList;

    /**
     * SDK钱包菜单列表(前端页面展示用)
     */
    @ApiModelProperty("SDK钱包菜单列表")
    private List<GameConfigData> sdkWalletMenuList;

    /**
     * SDK虚拟钱包菜单列表(前端页面展示用)
     */
    @ApiModelProperty("SDK虚拟钱包菜单列表")
    private List<GameConfigData> sdkVirtualWalletMenuList;

    /**
     * 游戏支付方式列表(前端页面展示用)
     */
    @ApiModelProperty("游戏支付方式列表")
    private List<GameConfigData> gamePayTypeList;

    /**
     * 苹果应用类支付条件列表(前端页面展示用)
     */
    @ApiModelProperty("苹果应用类支付条件列表")
    private List<GameConfigData> appIosPaymentTermList;

    /**
     * 事件广播列表(前端页面展示用)
     */
    @ApiModelProperty("事件广播列表")
    private List<GameConfigData> eventBroadcastList;

}

