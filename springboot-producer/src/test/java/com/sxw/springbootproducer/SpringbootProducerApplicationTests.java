package com.sxw.springbootproducer;

import com.sxw.entity.Order;
import com.sxw.springbootproducer.producer.RabbitOrderSender;
import com.sxw.springbootproducer.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootProducerApplicationTests {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Test
    public void testSend() throws Exception {
        for (int i = 0; i < 100; i++) {
            sendMessage(i);
        }
    }

    private void sendMessage(Integer tag) throws Exception {
        Order order = new Order();
        order.setId(tag);
        order.setName("测试订单_" + tag);
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());

        // 发送消息
        rabbitOrderSender.sendOrder(order);
    }

}
