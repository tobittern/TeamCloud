package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 礼包配置业务对象 gift_bag
 *
 * @author euler
 * @date 2022-06-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包配置业务对象")
public class GiftBagDto extends PageQuery {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id")
    private Long gameId;

    /**
     * 游戏名称
     */
    @ApiModelProperty(value = "游戏名称")
    private String gameName;

    /**
     * 礼包名称
     */
    @ApiModelProperty(value = "礼包名称")
    private String giftName;

    /**
     * 有效期-开始时间
     */
    @ApiModelProperty(value = "有效期-开始时间")
    private Date startTime;

    /**
     * 有效期-结束时间
     */
    @ApiModelProperty(value = "有效期-结束时间")
    private Date endTime;

    /**
     * 状态，0待上架，1已上架，2已下架
     */
    @ApiModelProperty(value = "状态，0待上架，1已上架，2已下架")
    private Integer status;

    /**
     * 应用平台 1：android 2：ios 3：h5
     */
    @ApiModelProperty(value = "应用平台")
    private String applicationType;

}
