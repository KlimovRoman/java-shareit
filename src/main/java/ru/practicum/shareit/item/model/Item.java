package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {
    private int id; //уникальный идентификатор вещи
    private String name; //краткое название
    private String description; //развёрнутое описание
    private Boolean available; //статус о том, доступна или нет вещь для аренды
    private User owner; //владелец вещи

    public Item(int id, String name, String description, Boolean available, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
    }
}