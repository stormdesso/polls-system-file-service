package ru.pstu.polls_system.file_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file.settings")
@Getter
public class FileProperties { //todo: можно вынести в БД

    @Value("${file.settings.maximum-files-per-poll}")
    private Long maxFilesPerPoll;

    @Value("${file.settings.download.list.min}")
    private Long downloadMinSize;

    @Value("${file.settings.download.list.max}")
    private Long downloadLimit;

    @Value("${file.settings.upload.sizeInMb}")
    private Double uploadMaxSizeInMb;

    @Value("${file.settings.upload.list.min}")
    private Long uploadListMinSize;

    @Value("${file.settings.upload.list.max}")
    private Long uploadListMaxSize;

    public Double getUploadMaxSizeInBytes() {
        return uploadMaxSizeInMb * 1024 * 1024;
    }

}
