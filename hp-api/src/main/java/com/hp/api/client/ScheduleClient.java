package com.hp.api.client;

import com.hp.common.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("schedule-service")
public interface ScheduleClient {
    @PutMapping("/schedule/deduct")
    Result deduct(@RequestParam Integer id);
}
