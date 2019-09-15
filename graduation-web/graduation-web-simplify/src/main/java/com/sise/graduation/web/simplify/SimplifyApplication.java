package com.sise.graduation.web.simplify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName SimplifyApplication
 * @Description
 * @Author CCJ
 * @Date 2019/9/14 23:03
 **/
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.sise.graduation"})
public class SimplifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimplifyApplication.class,args);
        log.info("Simplify Application is running");
    }
}
