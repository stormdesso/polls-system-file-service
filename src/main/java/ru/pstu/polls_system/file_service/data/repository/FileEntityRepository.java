package ru.pstu.polls_system.file_service.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pstu.polls_system.file_service.data.model.FileEntity;

import java.util.List;

/**
 * Репозиторий для хранения файлов
 * */
@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity, Long>{
    @Query("select f from FileEntity as f where f.pollId = :pollId and f.id in :ids")
    List<FileEntity> findByPollIdAndIdIn(@Param("pollId") Long pollId, @Param("ids") List<Long> ids);
}
