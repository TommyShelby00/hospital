package com.hp.api.client;

import com.hp.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserClient {
    @PutMapping("/user/deduct")
    Result deduct(@RequestParam Integer id, @RequestParam Integer amount);
}
