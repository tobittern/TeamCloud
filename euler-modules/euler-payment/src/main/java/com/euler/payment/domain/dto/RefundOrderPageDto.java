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
public class RefundOrderPageDto extends PageQuery {


    @ApiModelProperty(value = "订单号")
    private String businessOrderId;

    @ApiModelProperty(value = "支付订单号")
    private String payOrderId;

    @ApiModelProperty("会员id")
    private Long memberId;

    @ApiModelProperty("会员手机号")
    private String memberMobile;


    @ApiModelProperty(value = "退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-退款任务关闭")
    private Integer state;

    @ApiModelProperty("会员昵称")
    private String memberNickName;

    @ApiModelProperty("下单开始时间")
    private Date beginTime;

    @ApiModelProperty("下单结束时间")
    private Date endTime;

}
