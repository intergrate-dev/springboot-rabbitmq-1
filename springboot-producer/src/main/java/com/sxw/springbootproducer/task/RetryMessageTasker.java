package com.sxw.springbootproducer.task;

import com.sxw.springbootproducer.producer.RabbitOrderSender;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
public class RetryMessageTasker {


    @Autowired
    private RabbitOrderSender rabbitOrderSender;


//    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend(){
        System.out.println("-----------定时任务开始-----------");
        //pull status = 0 and timeout message
        // retry processing
    }
}

