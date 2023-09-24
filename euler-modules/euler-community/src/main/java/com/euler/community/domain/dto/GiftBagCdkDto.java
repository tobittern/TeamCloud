package com.euler.community.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 礼包SDk数据视图对象 gift_bag_cdk
 *
 * @author euler
 * @date 2022-06-07
 */
@Data
@ApiModel("礼包SDk数据视图对象")
@ExcelIgnoreUnannotated
public class GiftBagCdkDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 礼包表id
     */
    @ApiModelProperty("礼包表id")
    private Long giftBagId;

    /**
     * 游戏id
     */
    @ApiModelProperty("游戏id")
    private Long gameId;

    /**
     * 领取礼包的用户id
     */
    @ApiModelProperty("领取礼包的用户id")
    private Long memberId;

    /**
     * 礼包码
     */
    @ApiModelProperty("礼包码")
    private String code;

    /**
     * 礼包状态，0：未使用，1：已使用
     */
    @ApiModelProperty("礼包状态，0：未使用，1：已使用")
    private Integer status;

}
