package ru.pstu.polls_system.file_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.pstu.polls_system.file_service.config.AppConfig;

@SpringBootApplication
@EnableFeignClients
@Import({AppConfig.class})
public class PollsSystemFileServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PollsSystemFileServiceApplication.class, args);
	}
}
