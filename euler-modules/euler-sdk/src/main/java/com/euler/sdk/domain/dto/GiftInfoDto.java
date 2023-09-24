package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 礼包业务对象 gift_info
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("礼包业务对象")
public class GiftInfoDto extends PageQuery {

    /**
     * 礼包id
     */
    @ApiModelProperty(value = "礼包id")
    private Integer id;

    /**
     * 礼包名称
     */
    @ApiModelProperty(value = "礼包名称")
    private String giftName;

    /**
     * 礼包组Id
     */
    @ApiModelProperty(value = "礼包组Id")
    private Integer giftGroupId;

    /**
     * 礼包领取等级
     */
    @ApiModelProperty(value = "礼包领取等级")
    private Integer receiveGrade;

    /**
     * 礼包类型 1:红包 2:平台币 3:积分
     */
    @ApiModelProperty(value = "礼包类型 1:红包 2:平台币 3:积分")
    private String type;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private String gameId;

}
