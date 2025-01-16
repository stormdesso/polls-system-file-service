package ru.pstu.polls_system.file_service.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pstu.polls_system.file_service.config.RabbitMQConfig;
import ru.pstu.polls_system.file_service.data.repository.FileMetaInfoRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final FileMetaInfoRepository fileMetaInfoRepository;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @Scheduled(cron = "${file.settings.delete.schedule.cron}")
    public void deleteFiles() {
        log.info("Запуск плановой задачи по удалению файлов");
        var files = fileMetaInfoRepository.findByIsDeletedFlag();
        fileMetaInfoRepository.deleteByIsDeletedFlag();
        rabbitTemplate.convertAndSend(RabbitMQConfig.LOGS_FANOUT_EXCHANGE, files);
        log.info("Задача по удалению файлов завершена");
    }
}
