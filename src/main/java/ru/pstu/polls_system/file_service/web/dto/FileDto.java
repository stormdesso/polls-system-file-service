package ru.pstu.polls_system.file_service.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "Прикреплённый файл к опросу")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto extends FileInfoDto {
    @Schema(description = "Данные")
    private byte[] data;
}
