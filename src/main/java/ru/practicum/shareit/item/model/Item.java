package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id; //уникальный идентификатор вещи
    private String name; //краткое название
    private String description; //развёрнутое описание
    private Boolean available; //статус о том, доступна или нет вещь для аренды
    private User owner; //владелец вещи
    private ItemRequest request; //если вещь была создана по запросу другого пользователя, то в этом поле будет храниться ссылка на соответствующий запрос

    public Item(int id, String name, String description, Boolean available, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = user;
        this.request = null;
    }
}