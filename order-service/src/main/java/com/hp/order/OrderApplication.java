package com.hp.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.hp.order.mapper")
@EnableFeignClients(basePackages = "com.hp.api.client")
@ComponentScan({"com.hp.common","com.hp.order","com.hp.api"})
@SpringBootApplication
@EnableScheduling
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
