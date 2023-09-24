package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 主渠道业务对象 channel
 *
 * @author euler
 * @date 2022-04-01
 */

@Data
@ApiModel("主渠道业务对象")
public class ChannelDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Integer id;

    /**
     * 渠道名
     */
    @ApiModelProperty(value = "渠道名", required = true)
    private String channelName;

}
