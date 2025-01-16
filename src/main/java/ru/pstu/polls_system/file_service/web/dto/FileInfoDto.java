package ru.pstu.polls_system.file_service.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о прикреплённом файле к опросу")
public class FileInfoDto {
    @Schema(description = "Идентификатор файла")
    private Long id;

    @Schema(description = "Название файла")
    private String originalName;

    @Schema(description = "Тип файла")
    private String type;

    @Schema(description = "Размер файла")
    private String size;

    @Schema(description = "Помечен на удаление")
    private Boolean isDeleted = false;
}
