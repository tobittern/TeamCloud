package com.euler.risk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 广告成本管理业务对象 advertising_cost
 *
 * @author euler
 * @date 2022-08-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("广告成本管理业务对象")
public class AdvertisingCostBo extends BaseEntity {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", required = true)
    @NotNull(message = "主键id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 广告平台
     */
    @ApiModelProperty(value = "广告平台 ", required = true)
    @NotBlank(message = "广告平台 不能为空", groups = {AddGroup.class, EditGroup.class})
    private String advertisingPlatform;

    /**
     * 广告成本日期
     */
    @ApiModelProperty(value = "广告成本日期", required = true)
    @NotBlank(message = "广告成本日期不能为空", groups = {AddGroup.class, EditGroup.class})
    private String costDate;

    /**
     * 成本，精确到小数点后两位
     */
    @ApiModelProperty(value = "成本，精确到小数点后两位", required = true)
    @NotNull(message = "成本，精确到小数点后两位不能为空", groups = {AddGroup.class, EditGroup.class})
    private Float cost;


    /**
     * 游戏id
     */
    @ApiModelProperty("游戏Id")
    @NotNull(message = "游戏Id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer gameId;


    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;


    /**
     * 媒体id
     */
    @ApiModelProperty("媒体id")
    @NotNull(message = "媒体id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer mediaId;


    /**
     * 媒体名称
     */
    @ApiModelProperty("游戏名称")
    private String mediaName;

    /**
     * 游戏运行平台 (1 安卓  2 ios  3 h5)
     */
    @ApiModelProperty("游戏运行平台 (1 安卓  2 ios  3 h5)")
    private Integer gameOperationPlatform;


}
