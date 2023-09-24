package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WalletLogPageDto extends PageQuery {
    /**
     * 会员id
     */
    private Long memberId;

    @ApiModelProperty(value = "钱包类型，1：正常钱包，2：虚拟钱包", required = true)
    private Integer walletType = 1;


    @ApiModelProperty(value = "钱包值变动类型，1-积分，2-余额，3-平台币，4-成长值", required = true)
    private Integer changeType;

    @ApiModelProperty(value = "游戏Id")
    private Integer gameId;
}
