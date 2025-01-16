package ru.pstu.polls_system.file_service.business.mapper;

import org.jetbrains.annotations.NotNull;
import ru.pstu.polls_system.file_service.data.model.FileEntity;
import ru.pstu.polls_system.file_service.data.model.FileMetaInfo;
import ru.pstu.polls_system.file_service.web.dto.FileDto;
import ru.pstu.polls_system.file_service.web.dto.FileInfoDto;

import java.util.Collection;
import java.util.List;

public class FileMapper {

    public static List<FileInfoDto> toFileInfoDtos(@NotNull List<FileEntity> fileEntities) {
        return (List<FileInfoDto>) fileEntities.stream().map(file -> FileInfoDto.builder()
                .id(file.getId())
                .isDeleted(file.getIsDeleted())
                .originalName(file.getOriginalName())
                .size(file.getSize())
                .type(file.getType())
                .build()).toList();
    }

    public static List<FileInfoDto> toFileMetaInfoDtos(@NotNull List<FileMetaInfo> fileEntities) {
        return (List<FileInfoDto>) fileEntities.stream().map(file -> FileInfoDto.builder()
                .id(file.getId())
                .isDeleted(file.getIsDeleted())
                .originalName(file.getOriginalName())
                .size(file.getSize())
                .type(file.getType())
                .build()).toList();
    }


    public static List<FileDto> toFileDtos(@NotNull Collection<FileEntity> fileEntities) {
        return (List<FileDto>) fileEntities.stream().map(file -> FileDto.builder()
                .id(file.getId())
                .isDeleted(file.getIsDeleted())
                .originalName(file.getOriginalName())
                .size(file.getSize())
                .type(file.getType())
                .data(file.getData())
                .build()).toList();
    }

}
