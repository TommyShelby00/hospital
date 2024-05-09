package com.hp.schedule.listener;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.api.vo.Doctor;
import com.hp.schedule.domain.po.Schedule;
import com.hp.schedule.service.IScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleListener {
    private final IScheduleService iScheduleService;

    private final RedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "doctor.orgId.change.queue", durable = "true"),
            exchange = @Exchange(name = "doctor.topic", type = ExchangeTypes.TOPIC),
            key = "orgId.change"
    ))
    public void listenOrgChange(Map<String,Integer> map){
        log.info("收到消息,更改排班科室信息");
        UpdateWrapper<Schedule> wrapper=new UpdateWrapper<>();
        wrapper.eq("doctor_id",map.get("docId"));
        wrapper.set("org_id",map.get("orgId"));
        iScheduleService.update(wrapper);
    }

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="schedule.remind.return.quene",durable = "true"),
            exchange = @Exchange(name = "schedule.topic",type = ExchangeTypes.TOPIC),
            key = "remind.return"
    ))
    //回退号资源
    public void back(Integer id){
        log.info("收到消息，开始回退号资源...,{}",id);
        UpdateWrapper<Schedule> wrapper=new UpdateWrapper<>();
        wrapper.setSql("remind=remind+1").eq("id",id);
        //清除缓存
        iScheduleService.update(wrapper);
        Schedule schedule = iScheduleService.getById(id);
        redisTemplate.delete("sch:"+schedule.getDoctorId()+","+schedule.getDate());
    }

}
