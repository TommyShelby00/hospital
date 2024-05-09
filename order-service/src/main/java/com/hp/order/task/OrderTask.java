package com.hp.order.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.order.domain.po.Order;
import com.hp.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private IOrderService iOrderService;


    //更改订单状态
    @Scheduled(cron = "0 0 0 * *  ? ") // 每天 00:00:00 执行
    public void updateOrderStatus() {
        log.info("执行定时任务");
        LocalDate today = LocalDate.now();
        // 获取今天之前的订单
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.le("date",today).eq("status",2);
        List<Order> orders = iOrderService.list(wrapper);
        for (Order order : orders) {
            order.setStatus(3);
            UpdateWrapper<Order> wrapper1=new UpdateWrapper<>();
            wrapper1.eq("id",order.getId());
            wrapper1.set("status",3);
            iOrderService.update(wrapper1); // 更新订单状态
        }
    }
}
