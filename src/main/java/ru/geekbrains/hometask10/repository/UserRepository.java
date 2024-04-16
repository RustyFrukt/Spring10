package ru.geekbrains.hometask10.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.hometask10.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    @Query("select u from UserEntity u where u.login = :login")
    Optional<UserEntity> findUserByLogin(String login);

}
