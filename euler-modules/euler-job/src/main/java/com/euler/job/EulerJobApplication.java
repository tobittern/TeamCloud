package com.euler.job;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 任务调度模块
 *
 * @author euler
 */
@EnableDubbo
@SpringBootApplication
public class EulerJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(EulerJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  任务调度模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }

}
