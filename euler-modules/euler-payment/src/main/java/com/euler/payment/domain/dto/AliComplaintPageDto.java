package com.euler.payment.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 阿里投诉分页业务对象 complaint
 *
 * @author euler
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("投诉分页业务对象")
public class AliComplaintPageDto extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 支付宝侧投诉单号
     */
    @ApiModelProperty(value = "支付宝侧投诉单号")
    private String complainEventId;

    /**
     *  投诉工单状态
     * 商家处理中：MERCHANT_PROCESSING
     * 商家已反馈：MERCHANT_FEEDBACKED
     * 投诉已完结：FINISHED
     * 投诉已撤销：CANCELLED
     * 平台处理中：PLATFORM_PROCESSING
     * 平台处理完结：PLATFORM_FINISH
     * 系统关闭：CLOSED
     */
    @ApiModelProperty(value = " 投诉工单状态")
    private String status;

    /**
     * 查询开始时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     * 若不填写，则默认当前时间30天前。
     * 注意：begin_time和end_time时间跨度最大一年
     */
    @ApiModelProperty(value = "查询开始时间")
    private String beginTime;

    /**
     * 查询结束时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     * 若不填写，则默认当前时间。
     * 注意：begin_time和end_time时间跨度最大一年
     */
    @ApiModelProperty(value = "查询结束时间")
    private String endTime;

}
