package com.euler.community.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 弹窗管理业务对象 popup
 *
 * @author euler
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("弹窗管理业务对象")
public class PopupDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long memberId;

    /**
     * 专题名称
     */
    @ApiModelProperty(value = "专题名称")
    private String title;

    /**
     * 每天显示次数
     */
    @ApiModelProperty(value = "每天显示次数")
    private Integer times;

    /**
     * 弹窗显示开始时间
     */
    @ApiModelProperty(value = "弹窗显示开始时间")
    private String startTime;

    /**
     * 弹窗显示结束时间
     */
    @ApiModelProperty(value = "弹窗显示结束时间")
    private String endTime;

    /**
     * 跳转url
     */
    @ApiModelProperty(value = "跳转url")
    private String jumpUrl;

    /**
     * 显示优先级，默认值0，数字越小，显示级别越高
     */
    @ApiModelProperty(value = "显示优先级，默认值0，数字越小，显示级别越高")
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "弹窗状态，0待启用，1已启用，2已停用")
    private String status;

    /**
     * 弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单
     */
    @ApiModelProperty(value = "弹窗位置,1动态菜单，2发现菜单，3消息菜单，4个人菜单")
    private String position;

}
