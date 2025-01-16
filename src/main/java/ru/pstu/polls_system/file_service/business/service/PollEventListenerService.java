package ru.pstu.polls_system.file_service.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.pstu.polls_system.file_service.data.repository.FileMetaInfoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollEventListenerService {

    private final FileMetaInfoRepository fileMetaInfoRepository;

    public static final String POLL_QUEUE = "pollQueue";

    @RabbitListener(queues = {POLL_QUEUE})
    public void receiveMessage(Long pollId) {
        log.info("Удаление файлов, связанных с опросом с id:{}", pollId);
        fileMetaInfoRepository.deleteAllByPollId(pollId);
    }

}
