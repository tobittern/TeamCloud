package com.euler.community;

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
public class EulerCommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(EulerCommunityApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Community模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
