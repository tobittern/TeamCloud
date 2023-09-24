package com.euler.sdk.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 注销会员记录视图
 *
 * @author euler
 * @date 2022-03-29
 */
@Data
public class CancellationLogVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("主键")
    private Integer id;
    /**
     * 用户ID
     */
    @ApiModelProperty("主键")
    private Long memberId;

    /**
     * 注销原因
     */
    @ApiModelProperty("注销原因")
    private String reason;


    /**
     * 注销执行状态，0：未注销，1：注销成功，2：注销失败
     */
    @ApiModelProperty("注销执行状态")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 执行次数
     */
    @ApiModelProperty("执行次数")
    private Integer opNums = 0;

}
