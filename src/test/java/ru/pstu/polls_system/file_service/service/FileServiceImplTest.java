package ru.pstu.polls_system.file_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.pstu.polls_system.file_service.TestConfig;
import ru.pstu.polls_system.file_service.business.service.impl.FileServiceImpl;
import ru.pstu.polls_system.file_service.data.model.FileEntity;
import ru.pstu.polls_system.file_service.data.model.FileMetaInfo;
import ru.pstu.polls_system.file_service.data.repository.FileEntityRepository;
import ru.pstu.polls_system.file_service.data.repository.FileMetaInfoRepository;
import ru.pstu.polls_system.file_service.web.dto.FileInfoDto;
import ru.pstu.polls_system.file_service.web.feign.PermissionClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Import({TestConfig.class})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse(TestConfig.POSTGRES_15_ALPINE_IMAGE));

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Autowired
    private FileMetaInfoRepository fileMetaInfoRepository;

    @Autowired
    private FileServiceImpl fileService;

    @MockBean
    private PermissionClient permissionClient;

    @BeforeEach
    void setUp() {
        int entityCount = fileMetaInfoRepository.findAll().size();

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
                .pollId(1L)
                .originalName("File3")
                .type("image/png")
                .size("4")
                .isDeleted(true)
                .data("File3 content".getBytes())
                .build();

        FileEntity file4 = FileEntity.builder()
                .pollId(2L)
                .originalName("File4")
                .type("text/plain")
                .size("5")
                .isDeleted(false)
                .data("File4 content".getBytes())
                .build();

        assertEquals( entityCount + 4, fileEntityRepository.saveAll(List.of(file1, file2, file3, file4)).size());
    }

    @Test
    void getFilesInfosByPollIdTest() {
        List<FileInfoDto> result = fileService.getFilesInfosByPollId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(file -> "File1".equals(file.getOriginalName())));
        assertTrue(result.stream().anyMatch(file -> "File2".equals(file.getOriginalName())));
    }

    @Test
    void getFilesListByPollId_withInvalidSizeCollectionTest() {
        assertThrows(IllegalArgumentException.class, () -> fileService.getFilesListByPollId(1L, List.of()));
    }

    @Test
    void getFilesListByPollId_withInvalidIdsTest() {
        assertThrows(IllegalArgumentException.class, () -> fileService.getFilesListByPollId(1L, List.of(0L, -1L)));
    }

    @Test
    void saveAllFilesTest() {
        int entityCount = fileMetaInfoRepository.findAll().size();

        MultipartFile file1 = new MockMultipartFile("file1.txt", "file1.txt", "text/plain", "content".getBytes());
        MultipartFile file2 = new MockMultipartFile("file2.txt", "file2.txt", "text/plain", "content".getBytes());

        List<FileInfoDto> result = fileService.saveAll(List.of(file1, file2), 1L);

        assertNotNull(result);
        assertEquals(entityCount + 2, fileMetaInfoRepository.findAll().size());
        assertTrue(result.stream().anyMatch(file -> "file1.txt".equals(file.getOriginalName())));
        assertTrue(result.stream().anyMatch(file -> "file2.txt".equals(file.getOriginalName())));
    }

    @Test
    void saveAllFiles_withInvalidFileSizeTest() {
        MultipartFile largeFile = new MockMultipartFile("largeFile.txt", "largeFile.txt", "text/plain", new byte[1024 * 1024 * 11]);
        assertThrows(IllegalArgumentException.class, () -> fileService.saveAll(List.of(largeFile), 1L));
    }

    @Test
    void saveAllFiles_withInvalidSizeCollectionTest() {
        int numberOfFiles = 10;
        MultipartFile[] largeFiles = new MultipartFile[numberOfFiles];

        for (int i = 0; i < numberOfFiles; i++) {
            String fileName = "largeFile" + (i + 1) + ".txt";
            largeFiles[i] = new MockMultipartFile(fileName, fileName, "text/plain", "content".getBytes());
        }

        assertThrows(IllegalArgumentException.class, () -> fileService.saveAll(List.of(largeFiles), 1L));
    }

    @Test
    void saveAllFiles_withEmptyFileTest() {
        MultipartFile largeFile = new MockMultipartFile("largeFile.txt", "largeFile.txt", "text/plain", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> fileService.saveAll(List.of(largeFile), 1L));
    }

    @Test
    void saveAllFiles_withEmptyCollectionTest() {
        assertThrows(IllegalArgumentException.class, () -> fileService.saveAll(List.of(), 1L));
    }

    @Test
    void deleteFilesTest() {
        var files = fileMetaInfoRepository.findAll();
        assertTrue(files.size() >= 2);

        List<Long> fileIds = List.of(files.get(0).getId(), files.get(0).getId());
        fileService.deleteAll(fileIds);

        List<FileMetaInfo> deletedFiles = fileMetaInfoRepository.findAllById(fileIds);
        assertTrue(deletedFiles.stream().allMatch(FileMetaInfo::getIsDeleted));
    }

    @Test
    void deleteFiles_withInvalidCollectionSizeTest() {
        assertThrows(IllegalArgumentException.class, () -> fileService.deleteAll(List.of()));
    }

    @Test
    void deleteFiles_withInvalidIdsTest() {
        assertThrows(IllegalArgumentException.class, () -> fileService.deleteAll(List.of(0L, -1L)));
    }

    @AfterEach
    void clear() {
        fileMetaInfoRepository.deleteAll();
        assertEquals(0, fileMetaInfoRepository.count());
    }
}
