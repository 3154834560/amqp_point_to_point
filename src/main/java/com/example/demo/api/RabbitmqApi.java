package com.example.demo.api;

import com.example.demo.application.service.rabbitmq.sender.SenderService;
import com.example.demo.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author 王景阳
 * @date 2022/10/20 19:01
 */
@RestController
@RequestMapping("/rabbitmq")
public class RabbitmqApi {

    @Autowired
    private SenderService sender;

    @GetMapping("/sender/{queue}/{name}/{des}")
    public boolean sender(@PathVariable("queue") String queue, @PathVariable("name") String name, @PathVariable("des") String des) {
        User user = new User(name, des, LocalDateTime.now());
        System.out.println(user);
        return sender.send(queue, user);
    }

}
