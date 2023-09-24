package com.euler.risk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;



/**
 * 广告媒体业务对象 advertising_media
 *
 * @author euler
 * @date 2022-09-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("广告媒体业务对象")
public class AdvertisingMediaBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Integer id;

    /**
     * 媒体名称
     */
    @ApiModelProperty(value = "媒体名称", required = true)
    @NotBlank(message = "媒体名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String mediaName;


    /**
     * 广告平台
     */
    @ApiModelProperty(value = "广告平台 ", required = true)
    @NotBlank(message = "广告平台 不能为空", groups = { AddGroup.class, EditGroup.class })
    private String advertisingPlatform;

    /**
     * 返点比例
     */
    @ApiModelProperty(value = "返点比例", required = true)
    @NotNull(message = "返点比例不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer rebate;


}
