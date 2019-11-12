package com.sxw.springbootconsumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.sxw.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OrderReceiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 配置监听器
     * 在queue和exchange不存在的情况下，创建并建立绑定关系
     * routting-key设置： *表示匹配一个单词， #表示匹配一个或多个单词
     * concurrency  为队列绑定多个并发消费者的数量
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-queue", durable = "true"),
            exchange = @Exchange(name = "order-exchange", durable = "true", type = "topic"),
            key = "order.*"
    ), concurrency = "5"
    )

    /**
     * 处理消息并手动确认
     */
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String, Object> headers, Channel channel) {
        logger.info("Thread: {}, consumer ---------收到消息，开始消费---------", Thread.currentThread().getId());
        logger.info("consumer 订单, id: {},  info： {}", order.getId(), JSONObject.toJSONString(order));
        /**
         * Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
         * 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
         * RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
         */
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        try {

            // process message
            Thread.sleep(3000);

            channel.basicAck(deliveryTag, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //消息处理失败，重新放回队列
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        //手动确认一条消息已经被消费
        logger.info("consumer process end ......");
    }

}
