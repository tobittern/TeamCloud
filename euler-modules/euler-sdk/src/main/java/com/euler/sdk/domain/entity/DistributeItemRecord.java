package com.euler.sdk.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.euler.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 派发物品记录对象 distribute_item_record
 *
 * @author euler
 * @date 2022-04-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("distribute_item_record")
public class DistributeItemRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;
    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡
     */
    private String distributeType;
    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 派发数量
     */
    private BigDecimal distributeAmount;
    /**
     * 派发说明
     */
    private String remark;
    /**
     * 删除标志（0:未删除1:删除）
     */
    @TableLogic
    private String delFlag;

}
