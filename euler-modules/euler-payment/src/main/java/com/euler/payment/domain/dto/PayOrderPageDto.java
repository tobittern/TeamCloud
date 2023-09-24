package com.euler.payment.domain.dto;

import com.euler.common.mybatis.core.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("订单视图对象")
public class PayOrderPageDto extends PageQuery {



    @ApiModelProperty(value = "支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭")
    private Integer state;


    @ApiModelProperty("下单开始时间")
    private Date beginTime;

    @ApiModelProperty("下单结束时间")
    private Date endTime;

    @ApiModelProperty("业务订单id")
    private String orderId;

    @ApiModelProperty(value = "主渠道id")
    private Integer channelId;
}
