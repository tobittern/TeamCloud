package com.euler.sdk.domain.dto;

import com.euler.common.core.domain.dto.IdDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 礼包业务对象 gift_info
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("礼包业务对象")
public class GiftInfoDelDto {

    /**
     * 礼包id
     */
    @ApiModelProperty(value = "礼包id")
    String id;

    /**
     * 礼包组id
     */
    @ApiModelProperty(value = "礼包组id")
    Integer giftGroupId;
}
