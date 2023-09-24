package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 礼包组管理业务对象 gift_management
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@ApiModel("礼包组管理业务对象")
public class GiftManagementDto extends PageQuery {
    /**
     * 礼包组id
     */
    @ApiModelProperty(value = "礼包组id")
    private Integer id;

    /**
     * 礼包组名
     */
    @ApiModelProperty(value = "礼包组名")
    private String giftGroupName;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id")
    private String gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 是否上架 1:上架 2:下架
     */
    @ApiModelProperty(value = "是否上架 1:上架 2:下架")
    private String isUp;

    /**
     * 礼包组图标
     */
    @ApiModelProperty(value = "礼包组图标")
    private String giftGroupIcon;

}
