package com.euler.sdk.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 弹窗管理分页业务对象 popup
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("弹窗管理分页业务对象")
public class PopupPageDto extends PageQuery {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
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
    private String title;

    /**
     * 弹框类型 1强退  2奖励 3运营
     */
    @ApiModelProperty(value = "弹框类型 1强退  2奖励 3运营 ", required = true)
    private Integer type;

    /**
     * 开始次数
     */
    @ApiModelProperty(value = "开始次数", required = true)
    private Integer startTimes;

    /**
     * 结束次数
     */
    @ApiModelProperty(value = "结束次数", required = true)
    private Integer endTimes;

    /**
     * 弹窗显示开始时间
     */
    @ApiModelProperty(value = "弹窗显示开始时间", required = true)
    private String startTime;

    /**
     * 弹窗显示结束时间
     */
    @ApiModelProperty(value = "弹窗显示结束时间", required = true)
    private String endTime;

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
    private Integer showType;

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
    private Integer level;

    /**
     * 弹窗状态，0待启用，1已启用，2已停用
     */
    @ApiModelProperty(value = "弹窗状态，0待启用，1已启用，2已停用", required = true)
    private String status;

    /**
     * 礼包ID
     */
    @ApiModelProperty(value = "礼包ID", required = true)
    private Integer giftBagId;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 是否删除，0未删除，2已删除
     */
    @ApiModelProperty(value = "是否删除，0未删除，2已删除")
    private String delFlag;


}
