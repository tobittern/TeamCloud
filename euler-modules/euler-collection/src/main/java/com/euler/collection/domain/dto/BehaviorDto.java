package com.euler.collection.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 业务对象 behavior
 *
 * @author euler
 * @date 2022-03-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class BehaviorDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    private Integer id;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id", required = true)
    private Integer bizId;

    /**
     * 行为类型 （1设备 2网络 3行为）
     */
    @ApiModelProperty(value = "行为类型 （1设备 2网络 3行为）", required = true)
    private String behaviorType;

    /**
     * 用户id，不登录为0
     */
    @ApiModelProperty(value = "用户id，不登录为0", required = true)
    private Long userId;

    /**
     * 用户一次访问的标识ID
     */
    @ApiModelProperty(value = "用户一次访问的标识ID", required = true)
    private String sessionId;

}
