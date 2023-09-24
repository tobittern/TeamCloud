package com.euler.payment.utils;

import com.euler.payment.domain.dto.PayChannelDto;
import com.euler.payment.enums.PayChannelEnum;
import org.springframework.stereotype.Component;

@Component
public class PayChannelUtils {

    public PayChannelDto convertPayChannel(String payChannelStr) {
        PayChannelDto payChannelDto = new PayChannelDto();
        String[] arr = payChannelStr.split("_");
        String payChannel=arr[0];
        payChannelDto.setPayChannel(payChannel);
        if (payChannel.equals(PayChannelEnum.WALLET.getPayChannel())) {
        } else    {
            String payTypeStr=arr[1];
            payChannelDto.setPayType("app");
            if ("WebMobilePay".equals(payTypeStr)) {
                payChannelDto.setPayType("h5");
            } else if("iap".equals(payTypeStr)) {
                payChannelDto.setPayType("iap");
            }
        }
        payChannelDto.setPayChannelCode(payChannelStr);

        return payChannelDto;
    }

}
