package com.hp.api.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CustomFeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 获取前一个请求的HttpServletRequest对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 从前一个请求的头部获取您想要的信息，例如token
            String token = request.getHeader("token");
            // 将获取的信息设置到新的请求头中
            template.header("token", token);
        }
    }
}

