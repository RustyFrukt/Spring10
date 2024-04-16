package ru.geekbrains.hometask10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.hometask10.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity,Long> {
}
