package com.sise.graduation.web.zhoujx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.sise.graduation"})
public class UniversityFixedAssetsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityFixedAssetsApplication.class, args);
        System.out.println("UniversityFixedAssetsApplication");
    }

}
