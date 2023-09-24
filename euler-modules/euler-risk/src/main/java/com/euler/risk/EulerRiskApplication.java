package com.euler.risk;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 风控服务
 *
 * @author euler
 */
@EnableDubbo
@SpringBootApplication
public class EulerRiskApplication {
    public static void main(String[] args) {
        SpringApplication.run(EulerRiskApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  风控服务模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
