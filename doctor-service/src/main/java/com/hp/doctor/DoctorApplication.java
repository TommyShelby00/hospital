package com.hp.doctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.hp.doctor.mapper")
@ComponentScan({"com.hp.common","com.hp.doctor","com.hp.api.config"})
@EnableFeignClients(basePackages = "com.hp.api.client")
public class DoctorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class,args);
    }
}