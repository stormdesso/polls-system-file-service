package ru.pstu.polls_system.file_service.business.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.pstu.polls_system.file_service.config.FileProperties;
import ru.pstu.polls_system.file_service.data.repository.FileMetaInfoRepository;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileValidator {
    private static final String ID = "id";
    private static final String FILE_ID_COLLECTION_NAME = "идентификаторы файлов для скачивания";
    private static final String FILE_ID_FOR_DELETE_COLLECTION_NAME = "идентификаторы файлов для удаления";
    private static final String FILES_COLLECTION = "файлы";

    private final FileProperties fileProperties;

    private final FileMetaInfoRepository fileMetaInfoRepository;

    public void validateRequestedFiles(List<Long> ids) {
        validateSizeCollection(ids, FILE_ID_COLLECTION_NAME, fileProperties.getDownloadMinSize(), fileProperties.getDownloadLimit());
        ids.forEach(id -> validateMinValue(id, ID, 1L));
    }

    private void validateSizeCollection(final Collection<?> collection, final String collectionName, final Long minSize, Long maxSize) {
        validateMinSizeCollection(collection, collectionName, minSize);
        validateMaxSizeCollection(collection, collectionName, maxSize);
    }

    private void validateMaxSizeCollection(Collection<?> collection, String collectionName, Long maxSize) {
        if (collection.size() > maxSize) {
            throw throwMaxCollectionSizeException(maxSize, collectionName);
        }
    }

    private void validateMinSizeCollection(Collection<?> collection, String collectionName, Long minSize) {
        if (collection.size() < minSize) {
            throw throwMinCollectionSizeException(minSize, collectionName);
        }
    }

    private void validateMinValue(final Long value, final String title, final Long minValue) {
        if (value < minValue) {
            throw throwMinValueException(title, minValue);
        }
    }

    private IllegalArgumentException throwMinValueException(final String paramName, final Long value) {
        throw new IllegalArgumentException(String.format("Значение параметра %s не может быть меньше: %d", paramName, value));
    }

    private IllegalArgumentException throwMinCollectionSizeException(final Long value, final String collectionName) {
        throw new IllegalArgumentException(String.format("Количество элементов списка: %s не может быть меньше: %d", collectionName, value));
    }

    private IllegalArgumentException throwMaxCollectionSizeException(final Long value, final String collectionName) {
        throw new IllegalArgumentException(String.format("Количество элементов списка: %s не может быть больше: %d", collectionName, value));
    }

    public void validateBeforeUpload(List<MultipartFile> files, Long pollId) {
        files.forEach(file -> {
            validateFileMaxSize(file);
            validateFileNotEmpty(file);
        });
        validateSizeCollection(files, FILES_COLLECTION,
                fileProperties.getUploadListMinSize(), fileProperties.getUploadListMaxSize());

        validateFilesPerPollLimit(files, pollId);
    }

    private void validateFilesPerPollLimit(List<MultipartFile> files, Long pollId) {
        Long filesCount = fileMetaInfoRepository.countByPollId(pollId);
        if (filesCount + files.size() > fileProperties.getMaxFilesPerPoll()) {
            throw throwMaxFilesPerPollException(fileProperties.getMaxFilesPerPoll());
        }
    }

    private void validateFileMaxSize(MultipartFile multipartFile) {
        if (multipartFile.getSize() > fileProperties.getUploadMaxSizeInBytes()) {
            throw throwMaxFileSizeException(multipartFile.getOriginalFilename(), fileProperties.getUploadMaxSizeInMb());
        }
    }

    private void validateFileNotEmpty(MultipartFile multipartFile) {
        if (multipartFile.getSize() <= 0L) {
            throw throwFileEmptyException(multipartFile.getOriginalFilename());
        }
    }

    private IllegalArgumentException throwMaxFileSizeException(final String filename, final Double value) {
        throw new IllegalArgumentException(String.format("Размер файла %s не может превышать: %f MB", filename, value));
    }

    private IllegalArgumentException throwFileEmptyException(final String filename) {
        throw new IllegalArgumentException(String.format("Файл %s не может быть пустым!", filename));
    }

    private IllegalArgumentException throwMaxFilesPerPollException(final Long value) {
        throw new IllegalArgumentException(String.format("Максимальное количество файлов, прикреплённых к опросу, не может превышать: %d", value));
    }

    public void validateBeforeDelete(List<Long> filesIds) {
        validateMinSizeCollection(filesIds, FILE_ID_FOR_DELETE_COLLECTION_NAME, 1L);
        filesIds.forEach(id -> validateMinValue(id, ID, 1L));
    }
}
