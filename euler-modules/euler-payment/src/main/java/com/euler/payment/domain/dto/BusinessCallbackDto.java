package com.euler.payment.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BusinessCallbackDto {


    public final static String EVENT_PAY = "PAY";
    public final static String EVENT_REFUND = "REFUND";
    /**
     * 商品id
     */
    private String goodsId;


    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品数量
     */
    private Integer goodsNum;


    /**
     * 商品标题
     */
    private String subject;

    /**
     * 商品描述信息
     */
    private String body;


    /**
     * 支付平台订单id
     */
    private String payOrderId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;


    /**
     * 会员id
     */
    private Long memberId;


    /**
     * 业务订单id
     */
    private String outTradeNo;

    /**
     * 通知状态，SUCCESS，ERROR
     */
    private String status;
    /**
     * 通知类型，支付：PAY，退款：REFUND
     */
    private String eventType;

    /**
     * 通知id
     */
    private String notifyId;


    /**
     * 扩展数据，提交订单时的扩展数据
     */
    private String extData;


}
