package com.example.demo.application.service.rabbitmq.sender;

import com.example.demo.domain.entity.User;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建消息生产者Sender
 *
 * @author 王景阳
 * @date 2022/10/19 21:01
 */
@Service
public class SenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean send(String queue, User user) {
        MessagePostProcessor message = new MessagePostProcessor() {
            /**
             * 设置消息头中的消息
             */
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("headMsg", "wjy");
                // User类自动转化位字符串类型
                message.getMessageProperties().setHeader("headContent", user);
                return message;
            }
        };
        System.out.println("send: " + queue + " : " + user);
        rabbitTemplate.convertAndSend(queue, user, message);
        return true;
    }
}
