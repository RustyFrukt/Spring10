package ru.geekbrains.hometask10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.hometask10.entity.ReaderEntity;

public interface ReaderRepository extends JpaRepository<ReaderEntity,Long> {
}
