package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "items", schema = "public")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //уникальный идентификатор вещи
    private String name; //краткое название
    private String description; //развёрнутое описание
    private Boolean available; //статус о том, доступна или нет вещь для аренды
    @Column(name = "owner_id")
    private User owner; //владелец вещи

    public Item(int id, String name, String description, Boolean available, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
    }
}