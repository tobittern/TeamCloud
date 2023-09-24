package com.euler.common.payment.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AliComplaintQueryDto implements Serializable {



    private static final long serialVersionUID = 1L;


    /**
     * 分页大小
     */
    private Integer page_size;

    /**
     * 当前页数
     */
    private Integer page_num;


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
    private String status;

    /**
     * 查询开始时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     * 若不填写，则默认当前时间30天前。
     * 注意：begin_time和end_time时间跨度最大一年
     */

    private String begin_time;

    /**
     * 查询结束时间
     * 时间格式：yyyy-MM-dd HH:mm:ss
     * 若不填写，则默认当前时间。
     * 注意：begin_time和end_time时间跨度最大一年
     */
    private String end_time;

}
