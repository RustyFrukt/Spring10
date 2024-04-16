package ru.geekbrains.hometask10.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="books")
@Data
@Schema(name = "Книга")
public class BookEntity {

    public static long sequence = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "book_seq", allocationSize = 1)
    private final long id;
    @Column(name = "name", length = 255)
    private String name;

    public BookEntity() {
        this.id = sequence++;
        name = null;
    }

    public BookEntity(String name) {
        this.id = sequence++;
        this.name = name;
    }

    @JsonCreator
    public BookEntity(long id, String name) {
        this.id = sequence++;
        this.name = name;
    }

    public static BookEntity ofName(String name) {
        BookEntity book = new BookEntity();
        book.setName(name);
        return book;
    }

}

