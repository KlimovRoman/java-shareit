package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    public Item addItem(Item item);

    public Optional<Item> getItem(int itemId);

    public Item updItem(ItemDto itemDto, int itemId);

    public List<ItemDto> getItemsByUser(int userId);

    public List<ItemDto> searchItems(String text);
}