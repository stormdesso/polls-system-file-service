package ru.pstu.polls_system.file_service.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name = "file")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetaInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "poll_id", nullable = false)
    private Long pollId;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
}
