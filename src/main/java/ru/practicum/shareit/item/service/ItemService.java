package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    public ItemDto addItem(int userId, ItemDto itemDtoToAdd);

    public ItemDto updItem(int userId, int itemId, ItemDto itemDtoToAdd);

    public ItemDto getItemById(int itemId);

    public List<ItemDto> getItemsByUser(int userId);

    public List<ItemDto> searchItems(String text);
}
