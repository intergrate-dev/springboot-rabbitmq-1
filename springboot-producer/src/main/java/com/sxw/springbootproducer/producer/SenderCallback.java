package com.sxw.springbootproducer.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class SenderCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 生产者消息确认，是否成功发送到exchange
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            //如果confirm返回成功 则进行更新
            logger.info("producer msg confirmed, messageId: {}", correlationData.getId());
        } else {
            //失败后补偿操作: 重试
            logger.error("producer 消息确认失败，重新发送......");
        }
    }


    /**
     * 仅当消息从exchange到消息队列失败时被调用
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("messageId: {}, exchange: {}, routingKey: {}, replyCode: {}, replyCode: {}",
                message.getMessageProperties().getMessageId(), exchange, routingKey, replyCode, replyCode);
    }
}
