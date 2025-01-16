package ru.pstu.polls_system.file_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.pstu.polls_system.file_service.TestConfig;
import ru.pstu.polls_system.file_service.business.service.ScheduledTaskService;
import ru.pstu.polls_system.file_service.data.model.FileEntity;
import ru.pstu.polls_system.file_service.data.repository.FileEntityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Testcontainers
@Import({TestConfig.class})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ScheduledTaskServiceTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(TestConfig.POSTGRES_15_ALPINE_IMAGE));

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @BeforeEach
    void setUp() {
        int entityCount = fileEntityRepository.findAll().size();

        FileEntity file1 = FileEntity.builder()
                .pollId(1L)
                .originalName("File1")
                .type("text/plain")
                .size("2")
                .isDeleted(false)
                .data("File1 content".getBytes())
                .build();

        FileEntity file2 = FileEntity.builder()
                .pollId(1L)
                .originalName("File2")
                .type("image/png")
                .size("3")
                .isDeleted(true)
                .data("File2 content".getBytes())
                .build();
        assertEquals( entityCount + 2, fileEntityRepository.saveAll(List.of(file1, file2)).size());
    }

    @Test
    void deleteTest() {
        assertFalse(fileEntityRepository.findAll().isEmpty());
        scheduledTaskService.deleteFiles();
        assertEquals(1, fileEntityRepository.count());
    }

    @AfterEach
    void clear() {
        fileEntityRepository.deleteAll();
        assertEquals(0, fileEntityRepository.count());
    }
}
