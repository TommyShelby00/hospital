package com.hp.order.service;

import com.hp.order.domain.po.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-02-15
 */
public interface IOrderService extends IService<Order> {

    void markPaySuccess(Integer id);

    List<Order> todoList(String userId);
}
