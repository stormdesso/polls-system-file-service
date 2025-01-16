package ru.pstu.polls_system.file_service.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.pstu.polls_system.file_service.business.mapper.FileMapper;
import ru.pstu.polls_system.file_service.business.service.FileService;
import ru.pstu.polls_system.file_service.business.validator.FileValidator;
import ru.pstu.polls_system.file_service.data.model.FileEntity;
import ru.pstu.polls_system.file_service.data.model.FileMetaInfo;
import ru.pstu.polls_system.file_service.data.repository.FileEntityRepository;
import ru.pstu.polls_system.file_service.data.repository.FileMetaInfoRepository;
import ru.pstu.polls_system.file_service.web.dto.FileDto;
import ru.pstu.polls_system.file_service.web.dto.FileInfoDto;
import ru.pstu.polls_system.file_service.web.feign.PermissionClient;

import java.io.IOException;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileEntityRepository fileEntityRepository;
    private final FileMetaInfoRepository fileMetaInfoRepository;
    private final PermissionClient permissionClient;

    private final FileValidator fileValidator;

    @Override
    public List<FileInfoDto> getFilesInfosByPollId(Long pollId) {
        permissionClient.hasPermission(List.of(pollId));
        List<FileMetaInfo> files = fileMetaInfoRepository.findByPollId(pollId);
        return FileMapper.toFileMetaInfoDtos(files);
    }

    @Override
    public List<FileDto> getFilesListByPollId(Long pollId, List<Long> requestedFilesIds) {
        fileValidator.validateRequestedFiles(requestedFilesIds);
        permissionClient.hasPermission(List.of(pollId));
        List<FileEntity> files = fileEntityRepository.findByPollIdAndIdIn(pollId, requestedFilesIds);
        return FileMapper.toFileDtos(files);
    }

    @Override
    @Transactional
    public List<FileInfoDto> saveAll(List<MultipartFile> files, Long pollId) {
        fileValidator.validateBeforeUpload(files, pollId);
        permissionClient.hasPermission(List.of(pollId));
        List<FileEntity> savedFiles = files.stream().map(file -> save(pollId, file)).toList();
        return FileMapper.toFileInfoDtos(savedFiles);
    }

    @Transactional
    public FileEntity save(Long pollId, MultipartFile file) {
        try {
            String extension = FilenameUtils.getExtension((file.getOriginalFilename()));
            FileEntity fileEntity = FileEntity.builder()
                    .pollId(pollId)
                    .originalName(file.getOriginalFilename())
                    .data(file.getBytes())
                    .type(!StringUtils.hasText(extension) ? "unknown" : extension)
                    .size(FileUtils.byteCountToDisplaySize(file.getSize()))
                    .isDeleted(false)
                    .build();
            return fileEntityRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    @Override
    @Transactional
    public void deleteAll(List<Long> filesIds) {
        fileValidator.validateBeforeDelete(filesIds);
        var fileMetaInfos = fileMetaInfoRepository.findAllById(filesIds);
        permissionClient.hasPermission(fileMetaInfos.stream().map(FileMetaInfo::getPollId).toList());
        fileMetaInfos.forEach(file -> file.setIsDeleted(true));
        fileMetaInfoRepository.saveAll(fileMetaInfos);
    }
}
