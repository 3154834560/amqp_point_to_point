package com.example.demo.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author 王景阳
 * @date 2022/10/19 21:22
 */
@Configuration(proxyBeanMethods = false)
public class AppBean {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value(value = "${rabbitmq.queue.name}")
    private String queueName;

    /**
     * 创建一个消息队列
     */
    @Bean
    public Queue helloQueue() {
        return new Queue(queueName);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    /**
     * 使用 Jackson 转化器原因：
     * 1. 使数据在远程以json的形式保存
     * 2. 使不同的客户端接受相同类型的数据时，使数据能正常转换
     */
    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, localDateTimeJsonSerializer())
                .deserializerByType(LocalDateTime.class, localDateTimeJsonDeserializer());
    }

    /**
     * NOTICE: 注意使用 秒级 时间戳
     */
    private JsonSerializer<LocalDateTime> localDateTimeJsonSerializer() {
        return new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime dateTime, JsonGenerator generator, SerializerProvider provider) throws IOException {
                if (dateTime != null) {
                    long epochSecond = dateTime.toEpochSecond(ZoneOffset.ofHours(8));
                    generator.writeNumber(epochSecond);
                }
            }
        };
    }

    /**
     * NOTICE: 注意使用 秒级 时间戳
     */
    private JsonDeserializer<LocalDateTime> localDateTimeJsonDeserializer() {
        return new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                long epochSecond = parser.getValueAsLong();
                return LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.ofHours(8));
            }
        };
    }

}
