package ru.geekbrains.hometask10.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="readers")
@Data
@Schema(name="Читатель")
public class ReaderEntity {

    public static long sequence = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "reader_seq", allocationSize = 1)
    private final long id;
    @Column(name = "name", length = 255)
    private String name;

    public ReaderEntity() {
        this.id = sequence++;
    }

    public ReaderEntity(String name) {
        this.id = sequence++;
        this.name = name;
    }

    @JsonCreator
    public ReaderEntity(long id, String name) {
        this.id = sequence++;
        this.name = name;
    }

    public static ReaderEntity ofName(String name) {
        ReaderEntity reader = new ReaderEntity();
        reader.setName(name);
        return reader;
    }
}

