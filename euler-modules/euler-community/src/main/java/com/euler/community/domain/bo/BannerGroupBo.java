package com.euler.community.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * banner组业务对象 banner_group
 *
 * @author euler
 * @date 2022-06-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("banner组业务对象")
public class BannerGroupBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 应用场景，0发现菜单，1个人中心
     */
    @ApiModelProperty(value = "应用场景，0发现菜单，1个人中心", required = true)
    @NotBlank(message = "应用场景，0发现菜单，1个人中心不能为空", groups = { AddGroup.class, EditGroup.class })
    private String applicationType;

    /**
     * banner组名
     */
    @ApiModelProperty(value = "banner组名", required = true)
    @NotBlank(message = "banner组名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String groupName;

    /**
     * 显示开始时间
     */
    @ApiModelProperty(value = "显示开始时间", required = true)
    @NotNull(message = "显示开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 显示结束时间
     */
    @ApiModelProperty(value = "显示结束时间", required = true)
    @NotNull(message = "显示结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * banner内容
     */
    @ApiModelProperty(value = "banner内容", required = true)
    @NotBlank(message = "banner内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String bannerContent;

    /**
     * banner数量
     */
    @ApiModelProperty(value = "banner数量")
    private Integer bannerNumber;

    /**
     * 状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "状态，0待启用，1已启用，2已停用")
    private String status;

}
