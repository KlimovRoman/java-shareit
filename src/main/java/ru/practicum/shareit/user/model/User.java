package ru.practicum.shareit.user.model;
import lombok.Data;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "public")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //уникальный идентификатор пользователя
    @Column(name = "name")
    private String name; //имя или логин пользователя
    @Column(name = "email", unique = true)
    private String email; //адрес электронной почты.два пользователя не могут иметь одинаковый адрес электронной почты

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public User() {

    }
}