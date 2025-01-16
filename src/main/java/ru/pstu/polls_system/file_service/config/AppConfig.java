package ru.pstu.polls_system.file_service.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({SecurityConfig.class, RabbitMQConfig.class, SwaggerConfig.class})
@EnableScheduling
@EnableConfigurationProperties({FileProperties.class})
public class AppConfig {
}
