package com.euler.community.controller;

import cn.hutool.core.convert.Convert;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.euler.community.service.IDynamicForwardService;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态转发Controller
 * 前端访问路由地址为:/community/forward
 * @author euler
 * @date 2022-06-20
 */
@Validated
@Api(value = "动态转发控制器", tags = {"动态转发管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/forward")
public class DynamicForwardController extends BaseController {

    private final IDynamicForwardService iDynamicForwardService;

    /**
     * 分享到微信/朋友圈
     */
    @SneakyThrows
    @ApiOperation("分享")
    @PostMapping("/share")
    public R share(@RequestParam("url") String url) {

        // 生成签名的随机字符串
        String nonceStr = RandomStringUtils.randomNumeric(16);
        // 签名时间戳
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        // 标签
        String ticket = "jsapi_ticket";

        // 注意这里参数名必须全部小写，且必须有序
        String string1 ="jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;

        // 签名
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(string1.getBytes("UTF-8"));
        String signature = Convert.toHex(crypt.digest());

        Map map =new HashMap<>();
        map.put("nonceStr", nonceStr);
        map.put("signature", signature);
        map.put("timestamp", timestamp);
        map.put("url", url);

        // 将map转成String
        String json = JsonUtils.toJsonString(map);

        return R.ok(json);
    }

}
