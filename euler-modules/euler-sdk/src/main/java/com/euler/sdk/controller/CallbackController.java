package com.euler.sdk.controller;

import com.euler.common.core.domain.R;
import com.euler.common.core.domain.dto.IdDto;
import com.euler.sdk.service.ICallbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@Api(value = "异步回调", tags = {"异步回调管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/callback")
public class CallbackController {

    private final ICallbackService iCallbackService;

    /**
     * 商品购买之后的回调
     */
    @ApiOperation("商品购买之后的回调")
    @PostMapping("/buyGoodsCallback")
    public R buyGoodsCallback(Integer goodsId,Integer gameId, Long userId) {
        return iCallbackService.handleBuyGoodsCallback(goodsId,gameId, userId);
    }

}
