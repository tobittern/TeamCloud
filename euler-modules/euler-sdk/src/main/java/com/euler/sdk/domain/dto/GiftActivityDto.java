package com.euler.sdk.domain.dto;

import com.euler.common.core.validate.EditGroup;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 礼包活动管理业务对象 gift_activity
 *
 * @author euler
 * @date 2022-03-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包活动管理业务对象")
public class GiftActivityDto extends PageQuery {

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID", required = true)
    private Integer gameId;

    /**
     * 礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包
     */
    @ApiModelProperty(value = "礼包类型 1首充礼包 2单笔充值 3累计充值 4到达等级 5累计在线时长（分）6新人礼包 7实名认证礼包", required = true)
    private String giftType;

    /**
     * 礼包名
     */
    @ApiModelProperty(value = "礼包名", required = true)
    private String giftName;

    /**
     * 是否上线 0上线 1下线
     */
    @ApiModelProperty(value = "是否上线 0上线 1下线", required = true)
    private Integer isOnline;

}
