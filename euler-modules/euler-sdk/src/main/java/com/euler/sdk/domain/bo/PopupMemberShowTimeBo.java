package com.euler.sdk.domain.bo;

import com.euler.common.core.validate.AddGroup;
import com.euler.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 每个用户对一个弹框的展示次数业务对象 popup_member_show_time
 *
 * @author euler
 * @date 2022-09-05
 */

@Data
@ApiModel("每个用户对一个弹框的展示次数业务对象")
public class PopupMemberShowTimeBo {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Integer id;

    /**
     * 弹框ID
     */
    @ApiModelProperty(value = "弹框ID", required = true)
    @NotNull(message = "弹框ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer popupId;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long memberId;

    /**
     * 启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
     */
    @ApiModelProperty(value = "启动时机")
    private String startOccasion;

    /**
     * 展示次数
     */
    @ApiModelProperty(value = "展示次数", required = true)
    @NotNull(message = "展示次数不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer time;

}
