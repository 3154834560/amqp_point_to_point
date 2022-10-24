package com.example.demo.application.service.rabbitmq;

import com.example.demo.DemoApplicationTests;
import com.example.demo.application.service.rabbitmq.sender.SenderService;
import com.example.demo.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @author 王景阳
 * @date 2022/10/19 21:26
 */
public class HelloTest extends DemoApplicationTests {

    @Autowired
    SenderService senderService;

    @Test
    public void test() {
        senderService.send("queue1", new User("fds", "fsd", LocalDateTime.now()));
    }
}
