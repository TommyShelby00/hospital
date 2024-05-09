package com.hp.order.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hp.api.client.DoctorClient;
import com.hp.api.client.ScheduleClient;
import com.hp.api.vo.Doctor;
import com.hp.api.vo.Organization;
import com.hp.common.domain.PageResult;
import com.hp.common.domain.Result;
import com.hp.order.domain.po.Order;
import com.hp.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-02-15
 */
@RestController
@RequestMapping("/order")
@Slf4j
//@SaCheckLogin
@RequiredArgsConstructor
@Api(tags= "订单相关接口")
public class OrderController {

    @Autowired
    IOrderService iOrderService;

    private final ScheduleClient scheduleClient;

    private final DoctorClient doctorClient;

    private final RabbitTemplate rabbitTemplate;

    @ApiOperation("创建订单")
    @PostMapping
    Result<Order> createOrder(@RequestBody Order order){
        log.info("创建订单:{}",order);
        iOrderService.save(order);
        scheduleClient.deduct(order.getSchId());
        return Result.success(order);
    }

    @ApiOperation("更改订单")
    @PutMapping
    private Result update(@RequestBody Order order){
        iOrderService.updateById(order);
        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    private Result cancel(@RequestBody Order order){
        iOrderService.updateById(order);
        //回退号资源
        Order o = iOrderService.getById(order.getId());
        rabbitTemplate.convertAndSend("schedule.topic","remind.return",o.getSchId());
        return Result.success();
    }

    @ApiOperation("删除订单")
    @DeleteMapping
    private Result delete(@RequestBody List<Integer> ids){
        iOrderService.removeByIds(ids);
        return Result.success();
    }

    @ApiOperation("查询订单")
    @GetMapping
    private Result<Order> getById(@RequestParam("id") Integer id){
        Order order = iOrderService.getById(id);
        return Result.success(order);
    }


    @ApiOperation("查询用户历史订单")
    @GetMapping("/query")
    private Result<List<Order>> list(String userId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Order> list = iOrderService.list(wrapper);
        return Result.success(list);
    }

    @ApiOperation("查询用户待完成预约")
    @GetMapping("/todo")
    private Result<List<Order>> todoList(String userId){
        List<Order> list= iOrderService.todoList(userId);
        return Result.success(list);
    }

    @ApiOperation("批量查询订单")
    @GetMapping("/page")
    private Result<PageResult> queryOrder(int pageNo, int pageSize, String name, Integer orgId, Integer doctorId,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        if (name!=null&&name!="")
            wrapper.like("name",name);
        if (orgId!=null)
            wrapper.like("org_id",orgId);
        if (doctorId!=null)
            wrapper.like("doctor_id",doctorId);
        if (date!=null)
            wrapper.eq("date",date);
        // 1.分页查询，new Page()的两个参数分别是：页码、每页大小
        Page<Order> p = iOrderService.page(new Page<>(pageNo,pageSize),wrapper);
        // 4.数据
        List<Order> records = p.getRecords();
        List<Order> ls=new ArrayList<>();
        records.forEach(i->{
            Doctor doc = doctorClient.getById(i.getDoctorId()).getData();
            Organization org = doctorClient.getById1(i.getOrgId()).getData();
            Order order=new Order();
            BeanUtils.copyProperties(i,order);
            order.setDoctorName(doc.getName());
            order.setOrgName(org.getName());
            ls.add(order);
        });
        return Result.success(new PageResult(p.getTotal(),ls));
    }

    @GetMapping("/getByDocId")
    private Result<List<Order>> getByDocId(Integer doctorId){
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("doctor_id",doctorId);
        List<Order> list = iOrderService.list(wrapper);
        return Result.success(list);
    }
}
