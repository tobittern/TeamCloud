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
 * 业务对象 advert_view_record
 *
 * @author euler
 * @date 2022-06-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务对象")
public class AdvertViewRecordBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 广告表主键id
     */
    @ApiModelProperty(value = "广告表主键id", required = true)
    @NotNull(message = "广告表主键id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long advertId;

    /**
     * 查看广告的用户id
     */
    @ApiModelProperty(value = "查看广告的用户id", required = true)
    @NotNull(message = "查看广告的用户id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long memberId;

    /**
     * 查看广告的次数
     */
    @ApiModelProperty(value = "查看广告的次数", required = true)
    @NotNull(message = "查看广告的次数不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer viewNum;


}
