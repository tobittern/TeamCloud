package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 礼包领取记录业务对象 gift_receive_record
 *
 * @author euler
 * @date 2022-04-13
 */
@Data
@ApiModel("礼包领取记录业务对象")
public class GiftReceiveRecordDto extends PageQuery {

    /**
     * 礼包类型 1:等级礼包 2:活动礼包
     */
    @ApiModelProperty(value = "礼包类型 1:等级礼包 2:活动礼包")
    private String giftType;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 领取状态 0:未达成 1:待领取 2:已领取
     */
    @ApiModelProperty(value = "领取状态 0:未达成 1:待领取 2:已领取")
    private String receiveStatus;

    /**
     * 礼包id
     */
    @ApiModelProperty(value = "礼包id")
    private Integer giftId;

}
