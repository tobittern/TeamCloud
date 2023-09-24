package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 派发物品记录对象
 *
 * @author euler
 * @date 2022-03-21
 */
@Data
public class DistributeItemRecordDto extends PageQuery {

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡
     */
    @ApiModelProperty(value = "派发类型 1:积分 2:余额 3:平台币 4:成长值 5:年卡")
    private String distributeType;

}
