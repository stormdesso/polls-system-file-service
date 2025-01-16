package ru.pstu.polls_system.file_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.pstu.polls_system.file_service.business.aspect.HasPermission;
import ru.pstu.polls_system.file_service.business.service.FileService;
import ru.pstu.polls_system.file_service.web.dto.FileDto;
import ru.pstu.polls_system.file_service.web.dto.FileInfoDto;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static ru.pstu.polls_system.file_service.web.constant.ActionConstants.*;
import static ru.pstu.polls_system.file_service.web.constant.SystemObjectConstants.POLL;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(description = "Получить информацию о файлах, связанных с опросом (название, тип, вес)")
    @HasPermission(resource = POLL, action = READ)
    @GetMapping
    public List<FileInfoDto> getFilesInfoByPollId(
            @Parameter(description = "Идентификатор опроса")
            @RequestParam @Positive Long pollId) {
        return fileService.getFilesInfosByPollId(pollId);
    }

    @Operation(description = "Скачать файлы, связанные с опросом")
    @HasPermission(resource = POLL, action = READ)
    @PostMapping(value = "/download_list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FileDto>> downloadFilesList(
            @Parameter(description = "Идентификатор опроса")
            @RequestParam @Positive Long pollId,
            @Parameter(description = "Список идентификаторов файлов")
            @RequestParam List<Long> ids) {
        return new ResponseEntity<>(fileService.getFilesListByPollId(pollId, ids), OK);
    }

    @Operation(description = "Загрузить файлы на сервер")
    @HasPermission(resource = POLL, action = CREATE)
    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<FileInfoDto>> upload(@RequestBody MultipartFile[] files, @RequestParam @Positive Long pollId) {
        var savedFilesInfos = fileService.saveAll(List.of(files), pollId);
        return new ResponseEntity<>(savedFilesInfos, OK);
    }

    @Operation(description = "Удалить файлы по id")
    @HasPermission(resource = POLL, action = DELETE)
    @DeleteMapping(value = "/delete")
    public void delete(@Parameter(description = "Идентификаторы файлов") @RequestParam List<Long> ids) {
        fileService.deleteAll(ids);
    }
}
