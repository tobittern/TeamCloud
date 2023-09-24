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
 * 弹窗管理业务对象 popup
 *
 * @author euler
 * @date 2022-06-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("弹窗管理业务对象")
public class PopupBo extends BaseEntity {

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
     * 专题名称
     */
    @ApiModelProperty(value = "专题名称",required = true)
    @NotBlank(message = "专题名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 每天显示次数
     */
    @ApiModelProperty(value = "每天显示次数")
    private Integer times;

    /**
     * 弹窗显示开始时间
     */
    @ApiModelProperty(value = "弹窗显示开始时间", required = true)
    @NotNull(message = "弹窗显示开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 弹窗显示结束时间
     */
    @ApiModelProperty(value = "弹窗显示结束时间", required = true)
    @NotNull(message = "弹窗显示结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 弹窗图片
     */
    @ApiModelProperty(value = "弹窗图片", required = true)
    @NotBlank(message = "弹窗图片不能为空", groups = { AddGroup.class, EditGroup.class })
    private String popupIcon;

    /**
     * 跳转url
     */
    @ApiModelProperty(value = "跳转url", required = true)
    @NotBlank(message = "跳转url不能为空", groups = { AddGroup.class, EditGroup.class })
    private String jumpUrl;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ApiModelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高", required = true)
    @NotNull(message = "显示优先级，默认值0，数字越小，显示级别越高不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "弹窗状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单
     */
    @ApiModelProperty(value = "弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单", required = true)
    @NotBlank(message = "弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单不能为空", groups = { AddGroup.class, EditGroup.class })
    private String position;

}
