package com.example.demo.application.service.rabbitmq.recevier;

import com.example.demo.domain.entity.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * 创建消息消费者Receiver
 *
 * @author 王景阳
 * @date 2022/10/19 21:05
 */
@Service
public class ReceiverService {

    /**
     * @RabbitListener(queues = "queue1") 表示监听的消息队列名为 “queue1”
     * @Payload 表示接受消息体的消息
     * @Header 表示接受消息头的消息 ，消息头只能接受 string 类型参数
     */
    @RabbitListener(queues = "queue1")
    public void process1(@Payload User user, @Header(name = "headMsg") String headMsg, @Header(name = "headContent") String headContent) {
        System.out.println("Receiver1 body: " + user);
        System.out.println("Receiver1 head headMsg : " + headMsg);
        System.out.println("Receiver1 head headContent : " + headContent);
    }

    @RabbitListener(queues = "queue1")
    public void process2(@Payload User user, @Header(name = "headMsg") String headMsg, @Header(name = "headContent") String headContent) {
        System.out.println("Receiver2 body: " + user);
        System.out.println("Receiver2 head headMsg : " + headMsg);
        System.out.println("Receiver2 head headContent : " + headContent);
    }

}
