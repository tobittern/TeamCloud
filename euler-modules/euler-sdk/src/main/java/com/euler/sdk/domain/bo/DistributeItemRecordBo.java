package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 派发物品记录业务对象 distribute_item_record
 *
 * @author euler
 * @date 2022-04-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("派发物品记录业务对象")
public class DistributeItemRecordBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Integer id;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id", required = true)
    private Long memberId;

    /**
     * 派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡
     */
    @ApiModelProperty(value = "派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡", required = true)
    @NotBlank(message = "派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡不能为空", groups = { AddGroup.class })
    private String distributeType;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", required = true)
    private Integer goodsId;

    /**
     * 派发数量
     */
    @ApiModelProperty(value = "派发数量", required = true)
    @NotNull(message = "派发数量不能为空", groups = { AddGroup.class })
    private BigDecimal distributeAmount;

    /**
     * 派发说明
     */
    @ApiModelProperty(value = "派发说明", required = true)
    @NotBlank(message = "派发说明不能为空", groups = { AddGroup.class })
    private String remark;

}
