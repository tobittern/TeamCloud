package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;


/**
 * 投诉分页业务对象 complaint
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("投诉分页业务对象")
public class ComplaintPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     *  通知ID
     */
    @ApiModelProperty(value = " 通知ID")
    private String noticeId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String complaintCreateTime;

    /**
     * 通知类型
     */
    @ApiModelProperty(value = "通知类型")
    private String eventType;

    /**
     * 通知数据类型
     */
    @ApiModelProperty(value = "通知数据类型")
    private String resourceType;

    /**
     * 回调摘要
     */
    @ApiModelProperty(value = "回调摘要")
    private String summary;

    /**
     * 投诉单号
     */
    @ApiModelProperty(value = "投诉单号")
    private String complaintId;

    /**
     * 动作类型
     */
    @ApiModelProperty(value = "动作类型")
    private String actionType;


    /**
    * 开始时间
    */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
