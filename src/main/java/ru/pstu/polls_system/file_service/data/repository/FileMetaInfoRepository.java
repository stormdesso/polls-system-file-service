package ru.pstu.polls_system.file_service.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pstu.polls_system.file_service.data.model.FileMetaInfo;

import java.util.List;

@Repository
public interface FileMetaInfoRepository extends JpaRepository<FileMetaInfo, Long>{

    @Query("select f from FileMetaInfo as f where f.pollId = :pollId and f.isDeleted = false")
    List<FileMetaInfo> findByPollId(@Param("pollId") Long pollId);

    @Query("select count(f) from FileMetaInfo as f where f.pollId = :pollId and f.isDeleted = false")
    Long countByPollId(@Param("pollId") Long pollId);

    @Modifying
    @Query(value = "delete from FileMetaInfo as f where f.isDeleted = true")
    void deleteByIsDeletedFlag();

    @Query(value = "select f from FileMetaInfo as f where f.isDeleted = true")
    List<FileMetaInfo> findByIsDeletedFlag();

    void deleteAllByPollId(Long pollId);
}
