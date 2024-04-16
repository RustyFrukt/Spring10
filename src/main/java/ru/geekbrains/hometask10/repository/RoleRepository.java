package ru.geekbrains.hometask10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.hometask10.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
}
