package com.euler.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 邮箱相关配置
 *
 * @author euler
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfig {

    /**
     * 邮件服务器地址
     */
    private String host;

    /**
     * 发件人的email
     */
    private String username;

    /**
     * 发件人的密码
     */
    private String password;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 默认的编码
     */
    private String defaultEncoding;

    /**
     * 是否请求认证
     */
    private Boolean smtpAuth;

    /**
     * 是否使用SSL安全连接
     */
    private Boolean smtpSslEnable;

    /**
     * 指定实现javax.net.SocketFactory接口的类的接口
     */
    private Integer smtpSocketFactoryPort;

    /**
     * 指定实现javax.net.SocketFactory接口的类的名称
     */
    private String smtpSocketFactoryClass;

    /**
     * 只处理SSL的连接, 对于非SSL的连接不做处理
     */
    private Boolean smtpSocketFactoryFallback;

}
