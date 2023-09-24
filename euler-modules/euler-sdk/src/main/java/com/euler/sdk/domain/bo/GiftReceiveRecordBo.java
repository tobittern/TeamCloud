package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 礼包领取记录业务对象 gift_receive_record
 *
 * @author euler
 * @date 2022-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包领取记录业务对象")
public class GiftReceiveRecordBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long memberId;

    /**
     * 礼包类型 1:等级礼包 2:活动礼包
     */
    @ApiModelProperty(value = "礼包类型 1:等级礼包 2:活动礼包", required = true)
    @NotBlank(message = "礼包类型 1:等级礼包 2:活动礼包不能为空", groups = { AddGroup.class})
    private String giftType;

    /**
     * 礼包id
     */
    @ApiModelProperty(value = "礼包id", required = true)
    @NotNull(message = "礼包id不能为空", groups = { AddGroup.class})
    private Integer giftId;

}
