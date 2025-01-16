package ru.pstu.polls_system.file_service;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
@ComponentScan(basePackages = {"ru.pstu.polls_system.file_service"})
public class TestConfig {
    public static final String POSTGRES_15_ALPINE_IMAGE = "postgres:15-alpine";

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new MockConnectionFactory());
    }
}
