package com.hp.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.order.domain.po.Order;
import com.hp.order.mapper.OrderMapper;
import com.hp.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-02-15
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void markPaySuccess(Integer id) {
        log.info("更改订单状态为已支付:{}",id);
        UpdateWrapper<Order> wrapper=new UpdateWrapper<Order>();
        wrapper.setSql("status=1").eq("id",id);
        update(wrapper);
    }

    @Override
    public List<Order> todoList(String userId) {
        QueryWrapper<Order> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("status",1);
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders;
    }
}
