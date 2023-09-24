package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.euler.common.mybatis.core.page.PageQuery;
import java.util.Date;


/**
 * 投诉回复分页业务对象 complaint_response
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("投诉回复分页业务对象")
public class ComplaintResponsePageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 投诉单号
     */
    @ApiModelProperty(value = "投诉单号")
    private String complaintId;

    /**
     * 回复内容
     */
    @ApiModelProperty(value = "回复内容")
    private String responseContent;

    /**
     * 回复图片 传输给微信的是一个数组
     */
    @ApiModelProperty(value = "回复图片 传输给微信的是一个数组")
    private String responseImages;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;

    /**
     * 跳转链接文案
     */
    @ApiModelProperty(value = "跳转链接文案")
    private String jumpUrlText;

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
