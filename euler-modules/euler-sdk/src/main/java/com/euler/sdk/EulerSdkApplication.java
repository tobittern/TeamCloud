package com.euler.sdk;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SDK模块
 *
 * @author euler
 */
@EnableDubbo
@SpringBootApplication
public class EulerSdkApplication {
    public static void main(String[] args) {
        SpringApplication.run(EulerSdkApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  SDK模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
