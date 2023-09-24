package com.euler.statistics.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 付费留存数据统计
 *
 * @author euler
 * @date 2022-10-10
 */
@Data
@TableName("payment_keep_statistics")
public class PaymentKeepStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 数据类型，1：充值数据，2：角色充值数据
     */
    private String dataType;

    /**
     * 充值类型：1、平台币，2、余额，3：游戏消费
     */
    private String rechargeType;

    /**
     * 订单类型：P、平台消费，G、游戏消费
     */
    private String orderType;

    /**
     * 商品类型：1、年费商品，2、充值商品，3、游戏商品
     */
    private String goodsType;

    /**
     * 数据批次号，yyyyMMddHHmmss
     */
    private Long batchNo;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 主渠道id
     */
    private Integer channelId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道code
     */
    private String packageCode;

    /**
     * 游戏id
     */
    private Integer gameId;

    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 运行平台 (1 安卓  2 ios  3 h5)
     */
    private String operationPlatform;

    /**
     * 付费日期
     */
    private Date paymentDate;

    /**
     * 注册日期
     */
    private Date registDate;

    /**
     * 登录日期
     */
    private Date loginDate;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 服务器id
     */
    private String serverId;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
