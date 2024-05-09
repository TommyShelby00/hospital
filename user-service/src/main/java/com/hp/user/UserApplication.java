package com.hp.user;

import com.hp.common.config.QRConfig;
import com.hp.common.utils.QRCodeUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.hp.user.mapper")
@EnableFeignClients(basePackages = "com.hp.api.client")
@Import(QRConfig.class)
@ComponentScan({"com.hp.common","com.hp.user","com.hp.api.config"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
