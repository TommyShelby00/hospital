package com.hp.pay.service;

import com.hp.pay.domain.po.Payorder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-02-16
 */
public interface IPayorderService extends IService<Payorder> {

//    void pay(Integer id, Short type);


    String createOrder(String valueOf, BigDecimal bigDecimal, String subject, String body);
}
