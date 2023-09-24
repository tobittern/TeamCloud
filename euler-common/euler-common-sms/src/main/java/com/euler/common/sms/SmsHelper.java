package com.euler.common.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dypnsapi.model.v20170525.GetMobileRequest;
import com.aliyuncs.dypnsapi.model.v20170525.GetMobileResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.euler.common.core.domain.R;
import com.euler.common.core.utils.JsonUtils;
import com.euler.common.sms.config.SmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsHelper {

    @Autowired
    private SmsConfig smsConfig;

    /**
     * 发送短信
     *
     * @param mobile           手机号
     * @param signName         签名
     * @param smsTemplateCode  模板
     * @param smsTemplateParsm 发送内容
     * @return
     */
    public Boolean sendSms(String mobile, String signName, String smsTemplateCode, String smsTemplateParsm) {
        DefaultProfile profile = DefaultProfile.getProfile(smsConfig.getRegionId(), smsConfig.getAliAccessKey(), smsConfig.getAliSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(mobile);//接收短信的手机号码
        request.setSignName(signName);//短信签名名称
        request.setTemplateCode(smsTemplateCode);//短信模板CODE
        request.setTemplateParam(smsTemplateParsm);//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            // 判断是否发送成功
            if (response.getCode() != null && response.getCode().equals("OK")) {
                return true;
            } else {
                log.error("发送短信失败，mobile：{}，sigName：{}，smsTemplateCode：{}，smsTemplateParsm：{}，返回值：{}", mobile, signName, smsTemplateCode, smsTemplateParsm, JsonUtils.toJsonString(response));

            }

        } catch (Exception e) {
            log.error("发送短信异常，mobile：{}，sigName：{}，smsTemplateCode：{}，smsTemplateParsm：{}", mobile, signName, smsTemplateCode, smsTemplateParsm, e);
        }
        return false;

    }


    /**
     * @param token
     * @return
     */
    public R getPhoneCode(String token) {
        DefaultProfile profile = DefaultProfile.getProfile(smsConfig.getRegionId(), smsConfig.getAliAccessKey(), smsConfig.getAliSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        GetMobileRequest request = new GetMobileRequest();
        request.setAccessToken(token);
        try {
            GetMobileResponse response = client.getAcsResponse(request);
            log.info("阿里云 -> response:{}", JsonUtils.toJsonString(response));
            if (response.getCode().equals("OK")) {
                return R.ok(response.getGetMobileResultDTO().getMobile());
            }
        } catch (Exception e) {
            log.error("阿里云获取一键登录报错 Err", e);
        }
        return R.fail();
    }
}
