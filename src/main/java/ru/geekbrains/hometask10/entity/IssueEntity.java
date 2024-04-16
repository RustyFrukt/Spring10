package ru.geekbrains.hometask10.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="issues")
@Data
@Schema(name = "Выдача")
public class IssueEntity {

    public static long sequence = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "issue_seq", allocationSize = 1)
    private final Long id;
    @Column(name = "bookId", nullable = false)
    private final Long bookId;
    @Column(name = "readerId", nullable = false)
    private final Long readerId;

    // Дата выдачи
    @Column(name = "issued_at")
    private final LocalDateTime issued_at;
    // Дата возврата
    @Column(name = "returned_at")
    private LocalDateTime returned_at;

    public IssueEntity(long bookId, long readerId) {
        this.id = sequence++;
        this.bookId = bookId;
        this.readerId = readerId;
        this.issued_at = LocalDateTime.now();
        this.returned_at = null;
    }

    public IssueEntity() {
        this.id = sequence++;
        this.bookId = null;
        this.readerId = null;
        this.issued_at = LocalDateTime.now();
    }

    @JsonCreator
    public IssueEntity(long id, long bookId, long readerId) {
        this.id = sequence++;
        this.bookId = bookId;
        this.readerId = readerId;
        this.issued_at = LocalDateTime.now();
        this.returned_at = null;
    }

}

