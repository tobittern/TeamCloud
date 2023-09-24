package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 搜索历史业务对象 history_search
 *
 * @author euler
 * @date 2022-06-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("搜索历史业务对象")
public class HistorySearchBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容", required = true)
    @NotBlank(message = "搜索内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String key;

    /**
     * 对于key的搜索次数
     */
    @ApiModelProperty(value = "对于key的搜索次数", required = true)
    private Integer num;

    /**
     * 搜索位置(0首页搜索,1发现页搜索)
     */
    @ApiModelProperty(value = "搜索位置(0首页搜索,1发现页搜索)", required = true)
    @NotNull(message = "搜索位置(0首页搜索,1发现页搜索)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer position;

    /**
     * 平台
     */
    private String operationPlatform;

}
