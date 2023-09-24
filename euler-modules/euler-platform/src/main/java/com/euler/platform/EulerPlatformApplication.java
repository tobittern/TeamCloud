package com.euler.platform;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 开放平台模块
 *
 * @author euler
 */
@EnableDubbo
@SpringBootApplication
public class EulerPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(EulerPlatformApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  开放平台模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
