package com.hp.pay.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hp.common.domain.Result;
import com.hp.pay.domain.po.Payorder;
import com.hp.pay.service.IPayorderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-02-16
 */
@Controller
@RequestMapping("/pay")
@SaCheckLogin
@Api(tags="支付相关接口")
@Slf4j
public class PayorderController {
    @Autowired
    IPayorderService iPayorderService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Value("${alipay.alipayPublicKey}")
    private String publicKey;

//    @ApiOperation("生成支付单")
//    @PostMapping
//    private Result createPayOrder(@RequestBody Payorder payorder){
//        iPayorderService.save(payorder);
//        return Result.success();
//    }

//    @ApiOperation("支付功能")
//    @PutMapping
//    private Result pay(@RequestParam Integer id,@RequestParam Short type){
//        iPayorderService.pay(id,type);
//        return Result.success();
//    }


    /**
     * 用户去支付
     * @param payorder 订单id
     * @return 支付结果，成功返回支付的html信息
     */
    @ResponseBody
    @PostMapping("/doPay")
    public Result<String> doPay(@RequestBody Payorder payorder){
        log.info("创建支付单:{}",payorder);
        iPayorderService.save(payorder.setType(3));
        BigDecimal bigDecimal = BigDecimal.valueOf(payorder.getAmount());
        String order = iPayorderService.createOrder(
                String.valueOf(payorder.getOrderId()),
                bigDecimal,
                "L医院",
                "挂号费用"
        );
        return order != null
                ? Result.success(order)
                : Result.error( "支付失败,请稍后重试");
    }

    /**
     * 支付宝同步回调接口
     * @return 返回支付结果
     */
    @GetMapping("/return")
    //@ResponseBody
    public String handleReturn(
            @RequestParam("out_trade_no") String out_trade_no,
            @RequestParam("total_amount") String total_amount,
            @RequestParam("trade_no") String trade_no,
            @RequestParam("sign") String sign,
            @RequestParam("sign_type") String sign_type,
            @RequestParam("charset")String charset,
            @RequestParam("method")String method,
            @RequestParam("auth_app_id")String auth_app_id,
            @RequestParam("version")String version,
            @RequestParam("app_id") String app_id,
            @RequestParam("seller_id") String seller_id,
            @RequestParam("timestamp") String timestamp
    ) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();
        params.put("sign", sign);
        params.put("out_trade_no", out_trade_no);
        params.put("total_amount", total_amount);
        params.put("trade_no", trade_no);
        params.put("sign_type", sign_type);
        params.put("charset", charset);
        params.put("method", method);
        params.put("auth_app_id", auth_app_id);
        params.put("version", version);
        params.put("app_id", app_id);
        params.put("seller_id",seller_id);
        params.put("timestamp", timestamp);
        //验证支付宝的签名，确保通知的真实性
        boolean isValid = AlipaySignature.verifyV1(params, publicKey, "UTF-8", "RSA2");
        if (!isValid)
            return "error";
        //支付成功，执行相关操作，例如更新订单状态
        Integer orderId = Integer.parseInt(out_trade_no);
        log.info("支付成功,订单id:{}",orderId);
        rabbitTemplate.convertAndSend("pay.topic", "pay.success", orderId);
        UpdateWrapper<Payorder> wrapper=new UpdateWrapper<>();
        wrapper.set("type",2);
        wrapper.eq("order_id",orderId);
        wrapper.set("pay_time", LocalDateTime.now());
        if (!iPayorderService.update(wrapper))
            return "error";
        return "index";
    }


}
