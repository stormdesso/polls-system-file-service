package ru.pstu.polls_system.file_service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.pstu.polls_system.file_service.TestConfig;
import ru.pstu.polls_system.file_service.data.model.FileEntity;
import ru.pstu.polls_system.file_service.data.model.FileMetaInfo;
import ru.pstu.polls_system.file_service.data.repository.FileEntityRepository;
import ru.pstu.polls_system.file_service.data.repository.FileMetaInfoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Import({TestConfig.class})
@ActiveProfiles("test")
public class FileMetaInfoRepositoryTest {
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(TestConfig.POSTGRES_15_ALPINE_IMAGE));

    @Autowired
    FileMetaInfoRepository fileMetaInfoRepository;

    @BeforeEach
    void setUp(@Autowired FileEntityRepository fileEntityRepository) {
        FileEntity meta1 = FileEntity.builder()
                .pollId(1L)
                .originalName("File1")
                .type("text/plain")
                .size("1")
                .isDeleted(false)
                .data("File1 content".getBytes())
                .build();

        FileEntity meta2 = FileEntity.builder()
                .pollId(1L)
                .originalName("File2")
                .type("image/png")
                .size("2")
                .isDeleted(true)
                .data("File2 content".getBytes())
                .build();

        FileEntity meta3 = FileEntity.builder()
                .pollId(2L)
                .originalName("File3")
                .type("image/png")
                .size("3")
                .isDeleted(false)
                .data("File3 content".getBytes())
                .build();

        FileEntity meta4 = FileEntity.builder()
                .pollId(3L)
                .originalName("File4")
                .type("text/plain")
                .size("5")
                .isDeleted(true)
                .data("File4 content".getBytes())
                .build();

        fileEntityRepository.saveAll(List.of(meta1, meta2, meta3, meta4));
        assertEquals( 4, fileMetaInfoRepository.findAll().size());
    }

    @Test
    void findByPollIdTest() {
        List<FileMetaInfo> result = fileMetaInfoRepository.findByPollId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(meta -> meta.getPollId().equals(1L) && !meta.getIsDeleted()));
    }

    @Test
    void countByPollIdTest() {
        Long count = fileMetaInfoRepository.countByPollId(1L);

        assertNotNull(count);
        assertEquals(1L, count);
    }

    @Test
    @Transactional
    void deleteByIsDeletedFlagTest() {
        fileMetaInfoRepository.deleteByIsDeletedFlag();

        List<FileMetaInfo> remainingFiles = fileMetaInfoRepository.findAll();

        assertNotNull(remainingFiles);
        assertTrue(remainingFiles.stream().noneMatch(FileMetaInfo::getIsDeleted));
        assertEquals(2, remainingFiles.size());
    }

    @AfterEach
    void clear() {
        fileMetaInfoRepository.deleteAll();
        assertEquals(0, fileMetaInfoRepository.findAll().size());
    }
}
