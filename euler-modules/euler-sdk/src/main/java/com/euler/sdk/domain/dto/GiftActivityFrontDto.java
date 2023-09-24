package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 礼包活动管理业务对象 gift_activity
 *
 * @author euler
 * @date 2022-03-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包活动管理业务对象")
public class GiftActivityFrontDto extends PageQuery {

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    /**
     * 礼包类型1:红包 2:平台币 3:积分
     */
    @ApiModelProperty(value = "礼包类型 1：首充礼包 2：单笔充值 3：累计充值 4到达等级 5累计在线时长（分）6新人礼包", required = true)
    private String giftType;

    /**
     * 礼包名
     */
    @ApiModelProperty(value = "礼包名", required = true)
    private String giftName;


    /**
     * 分包code
     */
    @ApiModelProperty(value = "分包code", required = true)
    private String packageCode;


}
