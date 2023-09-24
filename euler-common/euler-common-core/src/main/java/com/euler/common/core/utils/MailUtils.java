package com.euler.common.core.utils;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.euler.common.core.config.MailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 邮件发送工具类
 *
 * @author open
 */
@Slf4j
@Component
public class MailUtils {

    /**
     * 发送激活邮件
     *
     * @param tos        收件人
     * @param code       验证码
     * @param mailConfig 邮件的相关信息
     */
    public static void sendMail(ArrayList<String> tos, String code, MailConfig mailConfig) {
        try {
            MailAccount account = new MailAccount();
            account.setHost(mailConfig.getHost());
            account.setPort(mailConfig.getPort());
            account.setAuth(mailConfig.getSmtpAuth());
            account.setFrom(mailConfig.getUsername());
            account.setUser(mailConfig.getUsername());
            account.setPass(mailConfig.getPassword());
            account.setSslEnable(mailConfig.getSmtpSslEnable());
            account.setSocketFactoryClass(mailConfig.getSmtpSocketFactoryClass());
            account.setSocketFactoryPort(mailConfig.getSmtpSocketFactoryPort());
            account.setSocketFactoryFallback(mailConfig.getSmtpSocketFactoryFallback());
            String codeHtml = "<div id=\"mail-\"><h2>校验码:</h2><p style=\"color:#317EF6;font-size:30px;\">" + code + "</p><hr><div id=\"tips\" style=\"font-size: 18px;\"><p>验证码有效期为一小时，为不影响您的正常操作，请您及时完成验证。</p><p>如非本人操作请忽略此邮件</p></div><div style=\"float:right;margin-right: 30px;\"><img style=\"width:100px;height:100px;\" src=\"\" alt=\"\"></div></div>";
            String send = MailUtil.send(account, tos, "邮箱验证码", codeHtml, true);
            log.info("邮件发送返回值->:{}", send);

        } catch (Exception e) {
            log.info("邮件发送的时候报错", e);
        }
    }

}
