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
 * 弹窗管理业务对象 popup
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("弹窗管理业务对象")
public class PopupBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long memberId;

    /**
     * 弹框名
     */
    @ApiModelProperty(value = "弹框名", required = true)
    @NotBlank(message = "弹框名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String title;

    /**
     * 弹框类型 1强退  2奖励 3运营
     */
    @ApiModelProperty(value = "弹框类型 1强退  2奖励 3运营 ", required = true)
    @NotNull(message = "弹框类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    /**
     * 每天显示次数 大约999就代表着一直持续
     */
    @ApiModelProperty(value = "每天显示次数 大约999就代表着一直持续", required = true)
    @NotNull(message = "每天显示次数不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer times;

    /**
     * 弹窗显示开始时间
     */
    @ApiModelProperty(value = "弹窗显示开始时间", required = true)
    @NotBlank(message = "弹窗显示开始时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private String startTime;

    /**
     * 弹窗显示结束时间
     */
    @ApiModelProperty(value = "弹窗显示结束时间", required = true)
    @NotBlank(message = "弹窗显示结束时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private String endTime;

    /**
     * 展示类型 1图片 2文本
     */
    @ApiModelProperty(value = "展示类型 1图片 2文本", required = true)
    private Integer showType;

    /**
     * 图片横
     */
    @ApiModelProperty(value = "图片横", required = true)
    private String pictureTransverse;

    /**
     * 图片纵
     */
    @ApiModelProperty(value = "图片纵", required = true)
    private String pictureLongitudinal;

    /**
     * 展示内容
     */
    @ApiModelProperty(value = "展示内容", required = true)
    private String showContent;

    /**
     * 跳转url
     */
    @ApiModelProperty(value = "跳转url", required = true)
    private String jumpUrl;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ApiModelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高", required = true)
    @NotNull(message = "显示优先级不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "弹窗状态，0待启用，1已启用，2已停用")
    private String status = "0";

    /**
     * 游戏ID
     */
    @ApiModelProperty(value = "游戏ID")
    private String gameIds;

    /**
     * 礼包ID
     */
    @ApiModelProperty(value = "礼包ID")
    private Integer giftBagId;

    /**
     * 启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
     */
    @NotBlank(message = "启动时机不能为空", groups = {AddGroup.class, EditGroup.class})
    @ApiModelProperty(value = "启动时机", required = true)
    private String startOccasion;

    /**
     * 每次启动类型(0:打开App 1:进入游戏)
     */
    @ApiModelProperty(value = "每次启动类型(0:打开App 1:进入游戏)")
    private String everyStartupType;

    /**
     * 弹窗时间，单位：秒
     */
    @ApiModelProperty(value = "弹窗时间，单位：秒")
    private Integer popupTime = 0;

    /**
     * 角色注册时间，单位：天，-1默认不限制
     */
    @ApiModelProperty(value = "角色注册时间，单位：天")
    private Integer roleRegistTime = -1;

    /**
     * 满足条件的值(启动时机选择【2:游戏到达等级 3:累计充值 4:累计在线时长(分)】时需要要填)
     */
    @ApiModelProperty(value = "满足条件的值")
    private String conditionValue;

}
