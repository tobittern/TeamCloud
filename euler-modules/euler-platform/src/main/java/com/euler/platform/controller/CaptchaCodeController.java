package com.euler.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import com.euler.common.core.config.MailConfig;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.core.utils.MailUtils;
import com.euler.common.core.utils.ServletUtils;
import com.euler.common.core.utils.StringUtils;
import com.euler.common.redis.utils.RedisUtils;
import com.euler.common.sms.SmsHelper;
import com.euler.platform.config.WebConfig;
import com.euler.platform.domain.bo.CaptchaCodeBo;
import com.euler.platform.domain.dto.GeeTestlBody;
import com.euler.platform.service.ICaptchaCodeService;
import com.euler.system.api.RemoteDictService;
import com.euler.system.api.domain.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮箱验证码的操作处理
 *
 * @author euler
 */
@Api(value = "发送邮箱验证码的操作处理", tags = {"发送邮箱验证码管理"})
@RequiredArgsConstructor
@RestController
@Slf4j
public class CaptchaCodeController {

    @Autowired
    private WebConfig webConfig;
    @Autowired
    private MailConfig mailConfig;
    @Autowired
    private LockTemplate lockTemplate;
    @Autowired
    private ICaptchaCodeService captchaCodeService;
    @Autowired
    private SmsHelper smsHelper;
    @DubboReference
    private RemoteDictService remoteDictService;


    @ApiOperation("发送验证码")
    @PostMapping("/sendCaptchaCode")
    public R sendEmailCode(@ApiParam("验证码") @RequestBody CaptchaCodeBo body) throws GeneralSecurityException {

        // 线下环境直接不发送验证码
        String code = "1111";
        // ip
        String ip = ServletUtils.getClientIP();
        if (webConfig.getIsShowCode()) {
            String lockKey = StringUtils.format("{}lock:captchacode:{}-{}", Constants.BASE_KEY, body.getReceiver(), body.getType());

            final LockInfo lockInfo = lockTemplate.lock(lockKey, 30000L, 5000L, RedissonLockExecutor.class);
            // 获取锁成功，处理业务
            try {
                if (null == lockInfo) {
                    return R.fail("业务处理中,请稍后再试");
                }
                // 进行基础功能验证
                String s = validEntityBeforeSave(body);
                if (s.equals("展示出图形验证码")) {
                    return R.ok(9001);
                } else if (!s.equals("success")) {
                    return R.fail(s);
                }
                // 基础验证通过了我们开始生成验证码
                // 6位随机验证码
                code = RandomStringUtils.randomNumeric(6);
                // 记录日志
                log.info("发送验证码: {}, ip：{}", body.getReceiver(), ip);


                if ("1".equals(body.getSendType())) {
                    // 支持群发 多个用逗号隔开
                    ArrayList<String> tos = CollUtil.newArrayList(body.getReceiver());
                    MailUtils.sendMail(tos, code, mailConfig);

                } else {
                    //发送手机验证码
                    Map<String, String> codeMap = new HashMap<>();
                    codeMap.put("code", code);
                    Boolean flag = smsHelper.sendSms(body.getReceiver(), webConfig.getSignName(), webConfig.getSmsTemplateCode(), JsonUtils.toJsonString(codeMap));
                    if (!flag)
                        return R.fail("发送验证码失败");
                }

                String limitOneMinuteKey = StringUtils.format("{}captcha:limit:one-minute:{}", Constants.BASE_KEY, body.getReceiver());

                RedisUtils.setCacheObject(limitOneMinuteKey, 1, Duration.ofMinutes(1));
            } finally {
                //释放锁
                lockTemplate.releaseLock(lockInfo);
            }
        }
        body.setCode(code);
        body.setIp(ip);
        body.setCreateBy(body.getReceiver());
        // 将验证码新增到数据库
        captchaCodeService.insertByBo(body);
        // 设置验证码缓存的有效期是15分钟
        RedisUtils.setCacheObject(Constants.CAPTCHA_CODE_EXPIRE_TIME + body.getReceiver(), code, Duration.ofMinutes(Constants.EMAIL_CODE_EXPIRATION));

        return R.ok("发送验证码成功！");
    }


    //region 检查极验
    @ApiOperation("检查极验")
    @PostMapping("/checkPolarTest")
    public R<Void> checkPolarTest(@ApiParam("邮箱") @RequestBody GeeTestlBody body) {
        // 1.初始化极验参数信息
        // 1.initialize geetest parameter
        String captchaId = webConfig.getGeetestCaptchaId();
        String captchaKey = webConfig.getGeetestCaptchaKey();
        String domain = webConfig.getGeetestDomain();

        // 2.获取用户验证后前端传过来的验证流水号等参数
        // 2.get the verification parameters passed from the front end after verification
        String lotNumber = body.getLotNumber();
        String captchaOutput = body.getCaptchaOutput();
        String passToken = body.getPassToken();
        String genTime = body.getGenTime();

        // 3.生成签名
        // 3.generate signature
        // 生成签名使用标准的hmac算法，使用用户当前完成验证的流水号lot_number作为原始消息message，使用客户验证私钥作为key
        // use standard hmac algorithms to generate signatures, and take the user's current verification serial number lot_number as the original message, and the client's verification private key as the key
        // 采用sha256散列算法将message和key进行单向散列生成最终的签名
        // use sha256 hash algorithm to hash message and key in one direction to generate the final signature
        String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);

        // 4.上传校验参数到极验二次验证接口, 校验用户验证状态
        // 4.upload verification parameters to the secondary verification interface of GeeTest to validate the user verification status
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("lot_number", lotNumber);
        queryParams.add("captcha_output", captchaOutput);
        queryParams.add("pass_token", passToken);
        queryParams.add("gen_time", genTime);
        queryParams.add("sign_token", signToken);
        // captcha_id 参数建议放在 url 后面, 方便请求异常时可以在日志中根据id快速定位到异常请求
        // geetest recommends to put captcha_id parameter after url, so that when a request exception occurs, it can be quickly located in the log according to the id
        String url = String.format(domain + "/validate" + "?captcha_id=%s", captchaId);
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        JSONObject jsonObject = new JSONObject();
        //注意处理接口异常情况，当请求极验二次验证接口异常时做出相应异常处理
        // pay attention to interface exceptions, and make corresponding exception handling when requesting GeeTest secondary verification interface exceptions or response status is not 200
        //保证不会因为接口请求超时或服务未响应而阻碍业务流程
        // website's business will not be interrupted due to interface request timeout or server not-responding
        try {
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(queryParams, headers);
            ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
            String resBody = response.getBody();
            jsonObject = JSON.parseObject(resBody);
        } catch (Exception e) {
            jsonObject.put("result", "success");
            jsonObject.put("reason", "request geetest api fail");
        }

        // 5.根据极验返回的用户验证状态, 网站主进行自己的业务逻辑
        // 5. taking the user authentication status returned from geetest into consideration, the website owner follows his own business logic
        if (jsonObject.getString("result").equals("success")) {
            // 存入当天永久有效
            String userIsAdoptPolarTest = "email:adopt:polar:test:" + body.getEmail();
            if (!RedisUtils.hasKey(userIsAdoptPolarTest)) {
                RedisUtils.setCacheObject(userIsAdoptPolarTest, 1, Duration.ofDays(1));
            }
            log.info("极验请求的结果数据【成功】 验证邮箱:{} reason:{}", body.getEmail(), jsonObject.getString("reason"));
            return R.ok();
        } else {
            log.info("极验请求的结果数据【失败】 验证邮箱:{} reason:{}", body.getEmail(), jsonObject.getString("reason"));
            return R.fail();
        }
    }
    //endregion 检查极验

    /**
     * 在发送验证码前首先进行基础的验证服务
     *
     * @param entity
     * @return
     */
    private String validEntityBeforeSave(CaptchaCodeBo entity) {
        // 首先验证当前用户60s内是否发送过信息
        String limitOneMinuteKey = StringUtils.format("{}captcha:limit:one-minute:{}", Constants.BASE_KEY, entity.getReceiver());
        Boolean aBoolean = RedisUtils.hasKey(limitOneMinuteKey);
        if (aBoolean) {
            return "60s后点击再次获取验证码";
        }
        //region 极验控制
        if ("2".equals(entity.getType())) {
            // 首先验证是否存在一直展示出验证码的逻辑
            // 五分钟之内连续获取3次 下次请求需要展示出图形验证码
            String isTodayAlwaysShowCode = StringUtils.format("{}captcha:today:always:show:code:{}", Constants.BASE_KEY, entity.getReceiver());
            // 获取当前邮箱是否已经执行过了极验 并且通过了
            String userIsAdoptPolarTest = StringUtils.format("{}captcha:adopt:polar:test:{}", Constants.BASE_KEY, entity.getReceiver());
            if (RedisUtils.hasKey(isTodayAlwaysShowCode) && !RedisUtils.hasKey(userIsAdoptPolarTest)) {
                // 当前存在一直展示出图形验证码 但是 并没有极验通过的表示 我们直接返回图形验证码
                return "展示出图形验证码";
            } else {

                // 不需要进行极验 获取需要进行极验 同时极验的结果也是正确的
                if (!RedisUtils.hasKey(isTodayAlwaysShowCode)) {
                    String fiveMinuteLimitKey = StringUtils.format("{}captcha:limit:five-minute:{}", Constants.BASE_KEY, entity.getReceiver());
                    Integer fiveMinuteInteger = Convert.toInt(RedisUtils.getCacheObject(fiveMinuteLimitKey), 0);
                    if (fiveMinuteInteger == 0) {
                        // 没有值我们进行添加
                        RedisUtils.setCacheObject(fiveMinuteLimitKey, 1, Duration.ofMinutes(5));
                    } else {
                        // 判断 是否存在极验的值 存在了 直接跳出设置极验的请求
                        if (fiveMinuteInteger >= 3) {
                            // 添加当天一直展示出图形验证码
                            RedisUtils.setCacheObject(isTodayAlwaysShowCode, 1, Duration.ofDays(1));
                            return "展示出图形验证码";
                        } else {
                            RedisUtils.setCacheObject(fiveMinuteLimitKey, (fiveMinuteInteger + 1), true);
                        }
                    }
                }

                // 需要删除掉极验的key
                RedisUtils.deleteObject(userIsAdoptPolarTest);

            }
        }
        //endregion 极验控制

        Map<String, Integer> limit = getLimit();
        Integer phoneLimit = 10;
        Integer ipLimit = 100;
        if (limit.containsKey("phone")) {
            phoneLimit = limit.get("phone");
        }
        if (limit.containsKey("ip")) {
            ipLimit = limit.get("ip");
        }
        // 获取当天请求的总次数
        String todayLimitKey = StringUtils.format("{}captcha:limit:today:{}", Constants.BASE_KEY, entity.getReceiver());
        Integer todayInteger = Convert.toInt(RedisUtils.getCacheObject(todayLimitKey), 0);
        if (todayInteger == 0) {
            // 没有值我们进行添加
            RedisUtils.setCacheObject(todayLimitKey, 1, Duration.ofDays(1));
        } else {
            // 数据存在 我们判断是否超过了10次 超过了返回错误
            if (todayInteger >= phoneLimit) {
                return "一天最多只能发送" + phoneLimit + "次验证码请求";
            } else {
                RedisUtils.setCacheObject(todayLimitKey, (todayInteger + 1), true);
            }
        }
        // 获取IP当前请求的总次数
        String ipLimitKey = StringUtils.format("{}captcha:limit:ip:{}", Constants.BASE_KEY, ServletUtils.getClientIP());
        Integer ipInteger = Convert.toInt(RedisUtils.getCacheObject(ipLimitKey), 0);
        if (ipInteger == 0) {
            // 没有值我们进行添加
            RedisUtils.setCacheObject(ipLimitKey, 1, Duration.ofDays(1));
        } else {
            // 数据存在 我们判断是否超过了100次 超过了返回错误
            if (ipInteger >= ipLimit) {
                return "同一个IP一天最多只能发送" + ipLimit + "次验证码请求";
            } else {
                RedisUtils.setCacheObject(ipLimitKey, (ipInteger + 1), true);
            }
        }

        return "success";
    }

    /**
     * 获取验证码请求限制
     *
     * @return
     */
    private Map<String, Integer> getLimit() {
        List<SysDictData> data = remoteDictService.selectDictDataByType("interception_configuration");
        HashMap<String, Integer> objectObjectHashMap = new HashMap<>();
        data.forEach(a -> {
            if (a.getDictLabel().equals("phone")) {
                objectObjectHashMap.put("phone", Convert.toInt(a.getDictValue()));
            } else if (a.getDictLabel().equals("ip")) {
                objectObjectHashMap.put("ip", Convert.toInt(a.getDictValue()));
            }
        });
        return objectObjectHashMap;
    }

}
