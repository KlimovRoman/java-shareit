package ru.practicum.shareit.user.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    private int id; //уникальный идентификатор пользователя
    private String name; //имя или логин пользователя
    private String email; //адрес электронной почты.два пользователя не могут иметь одинаковый адрес электронной почты

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}