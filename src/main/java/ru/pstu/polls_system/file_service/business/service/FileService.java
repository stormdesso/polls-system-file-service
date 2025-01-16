package ru.pstu.polls_system.file_service.business.service;

import org.springframework.web.multipart.MultipartFile;
import ru.pstu.polls_system.file_service.web.dto.FileDto;
import ru.pstu.polls_system.file_service.web.dto.FileInfoDto;

import java.util.List;

public interface FileService{
    List<FileInfoDto> getFilesInfosByPollId(Long pollId);
    List<FileDto> getFilesListByPollId(Long pollId, List<Long> filesIds);
    List<FileInfoDto> saveAll(List<MultipartFile> files, Long pollId);

    void deleteAll(List<Long> ids);
}
