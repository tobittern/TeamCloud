package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import com.euler.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

/**
 * 礼包管理业务对象 gift_management
 *
 * @author euler
 * @date 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("礼包管理业务对象")
public class GiftManagementBo extends BaseEntity {

    /**
     * 礼包组id
     */
    @ApiModelProperty(value = "礼包组id", required = true)
    private Integer id;

    /**
     * 礼包组名
     */
    @ApiModelProperty(value = "礼包组名", required = true)
    @NotBlank(message = "礼包组名不能为空", groups = { AddGroup.class})
    private String giftGroupName;

    /**
     * 游戏id
     */
    @ApiModelProperty(value = "游戏id", required = true)
    @NotBlank(message = "游戏id不能为空", groups = { AddGroup.class, EditGroup.class})
    private String gameId;

    /**
     * 礼包数量
     */
    @ApiModelProperty(value = "礼包数量", required = true)
    private Integer giftAmount;

    /**
     * 礼包领取等级
     */
    @ApiModelProperty(value = "礼包领取等级", required = true)
    private String receiveGrade;

    /**
     * 礼包组图标
     */
    @ApiModelProperty(value = "礼包组图标", required = true)
    @NotBlank(message = "礼包组图标不能为空", groups = { AddGroup.class, EditGroup.class})
    private String giftGroupIcon;

    /**
     * 是否上架 1:上架 2:下架
     */
    @ApiModelProperty(value = "是否上架 1:上架 2:下架", required = true)
    private String isUp = "2";

}
