package ru.pstu.polls_system.file_service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import ru.pstu.polls_system.file_service.data.model.FileEntity;
import ru.pstu.polls_system.file_service.data.repository.FileEntityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
@Import({TestConfig.class})
@ActiveProfiles("test")
public class FileEntityRepositoryTest {
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(TestConfig.POSTGRES_15_ALPINE_IMAGE));

    @Autowired
    FileEntityRepository fileEntityRepository;

    @BeforeEach
    void setUp() {
        var entityCount = 0;

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
                .isDeleted(false)
                .data("File2 content".getBytes())
                .build();

        FileEntity file3 = FileEntity.builder()
                .pollId(2L)
                .originalName("File3")
                .type("image/png")
                .size("4")
                .isDeleted(false)
                .data("File3 content".getBytes())
                .build();

        fileEntityRepository.saveAll(List.of(file1, file2, file3));
        assertEquals(entityCount + 3, fileEntityRepository.findAll().size());
    }

    @Test
    void findByPollIdAndIdInTest() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<FileEntity> result = fileEntityRepository.findByPollIdAndIdIn(1L, ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(file -> file.getPollId().equals(1L)));
    }

    @AfterEach
    void clear() {
        fileEntityRepository.deleteAll();
        assertEquals(0, fileEntityRepository.findAll().size());
    }
}
