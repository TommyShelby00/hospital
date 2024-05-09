package com.hp.schedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.hp.schedule.mapper")
@EnableFeignClients(basePackages = "com.hp.api.client")
@ComponentScan({"com.hp.common","com.hp.schedule","com.hp.api.config"})
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }
}