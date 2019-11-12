package com.sxw.springbootproducer.service;

import com.sxw.entity.Order;
import com.sxw.springbootproducer.producer.RabbitOrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    public void createOrder(Order order) throws Exception {
        // 发送消息
        rabbitOrderSender.sendOrder(order);
    }

}

