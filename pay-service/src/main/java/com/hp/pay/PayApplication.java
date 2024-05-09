package com.hp.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.hp.pay.mapper")
@ComponentScan({"com.hp.common.handler","com.hp.pay"})
@EnableFeignClients(basePackages = "com.hp.api.client")
@SpringBootApplication
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }
}
