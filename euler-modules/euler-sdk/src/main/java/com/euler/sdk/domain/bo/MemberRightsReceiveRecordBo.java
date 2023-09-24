package com.euler.sdk.domain.bo;

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
 * 会员权益领取记录业务对象 member_rights_receive_record
 *
 * @author euler
 * @date 2022-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("会员权益领取记录业务对象")
public class MemberRightsReceiveRecordBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id", required = true)
    private Long memberId;

    /**
     * 领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分
     */
    @ApiModelProperty(value = "领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分", required = true)
    @NotBlank(message = "领取类型 1:当月领取平台币 2:立即领取的平台币 3:积分不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 会员等级 1:初级 2：中级 3：高级
     */
    @ApiModelProperty(value = "会员等级 1:初级 2：中级 3：高级", required = true)
    @NotBlank(message = "会员等级 1:初级 2：中级 3：高级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String memberLevel;

}
