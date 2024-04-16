package ru.geekbrains.hometask10.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
@Schema(name = "Пользователь")
public class UserEntity {

    public static long sequence = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private final long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    public UserEntity() {
        this.id = sequence++;
        login = null;
    }

    public UserEntity(String login, String password, List<RoleEntity> roles) {
        this.id = sequence++;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    @JsonCreator
    public UserEntity(long id, String login, String password, List<RoleEntity> roles) {
        this.id = sequence++;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setLogin(String login) { this.login = login; }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name= "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<RoleEntity> roles;

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles.toString() +
                '}';
    }

}
