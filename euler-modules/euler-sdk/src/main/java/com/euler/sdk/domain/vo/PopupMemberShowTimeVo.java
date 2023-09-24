package com.euler.sdk.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 每个用户对一个弹框的展示次数视图对象 popup_member_show_time
 *
 * @author euler
 * @date 2022-09-05
 */
@Data
@ApiModel("每个用户对一个弹框的展示次数视图对象")
@ExcelIgnoreUnannotated
public class PopupMemberShowTimeVo  implements Serializable  {

    private static final long serialVersionUID = 1L;

    /**
     * 弹框ID
     */
    @ExcelProperty(value = "弹框ID")
    @ApiModelProperty("弹框ID")
    private Integer popupId;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)
     */
    @ExcelProperty(value = "启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)")
    @ApiModelProperty("启动时机(0:每次启动 1:首充 2:游戏到达等级 3:累计充值 4:累计在线时长(分) 5:实名认证 6:绑定手机号)")
    private String startOccasion;

    /**
     * 展示次数
     */
    @ExcelProperty(value = "展示次数")
    @ApiModelProperty("展示次数")
    private Integer time;

}
