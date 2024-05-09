package com.hp.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.hp.api.client.UserClient;
import com.hp.pay.domain.po.Payorder;
import com.hp.pay.mapper.PayorderMapper;
import com.hp.pay.service.IPayorderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-02-16
 */
@Service
@RequiredArgsConstructor
public class PayorderServiceImpl extends ServiceImpl<PayorderMapper, Payorder> implements IPayorderService {

    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.appPrivateKey}")
    private String privateKey;

    @Value("${alipay.alipayPublicKey}")
    private String publicKey;

    @Value("${alipay.gatewayUrl}")
    private String gatewayUrl;

//    @Override
//    public void pay(Integer id, Short type) {
//        Payorder payorder = getById(id);
//        //判断支付方式是否为余额
//        if (type==0){
//            userClient.deduct(payorder.getUserId(), payorder.getAmount());
//            //通过rabbitmq通知order-service更改订单状态
//            rabbitTemplate.convertAndSend("pay.topic", "pay.success", payorder.getOrderId());
//        }
//
//    }

    public String createOrder(String orderId, BigDecimal amount, String subject, String body) {
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, "json", "UTF-8", publicKey, "RSA2");
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl("http://localhost:8080/pay/return");
        request.setNotifyUrl("http://localhost/");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", orderId);
        jsonObject.put("total_amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP));
        jsonObject.put("subject", subject);
        jsonObject.put("body", body);
        jsonObject.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(jsonObject.toString());
        try {
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
