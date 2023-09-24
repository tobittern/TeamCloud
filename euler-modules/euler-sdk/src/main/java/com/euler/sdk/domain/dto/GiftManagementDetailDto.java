package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
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
public class GiftManagementDetailDto extends PageQuery {
    /**
     * 礼包组Id
     */
    @ApiModelProperty(value = "礼包组Id")
    private Integer giftGroupId;

    /**
     * 礼包组名称
     */
    @ApiModelProperty(value = "礼包组名称")
    private String giftGroupName;

}
