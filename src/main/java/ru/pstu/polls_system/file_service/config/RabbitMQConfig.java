package ru.pstu.polls_system.file_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class RabbitMQConfig {

    public static final String LOGS_QUEUE = "logsQueue";

    public static final String LOGS_FANOUT_EXCHANGE = "logsFanoutExchange";


    private final ConnectionFactory connectionFactory;


//    @PostConstruct
//    public void initializeConnection() {
//        connectionFactory.createConnection();
//    }

    @Bean
    public FanoutExchange logsFanoutExchange() {
        return new FanoutExchange(LOGS_FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public Queue logsQueue() {
        return new Queue(LOGS_QUEUE, true);
    }

    @Bean
    public Binding bindingPollQueue(@Qualifier(LOGS_QUEUE) Queue pollQueue, @Qualifier(LOGS_FANOUT_EXCHANGE) FanoutExchange logsFanoutExchange) {
        return BindingBuilder.bind(pollQueue).to(logsFanoutExchange);
    }
}
