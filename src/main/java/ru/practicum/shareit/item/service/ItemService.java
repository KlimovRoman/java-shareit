package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    public ItemDto addItem(int userId, ItemDto itemDtoToAdd);

    public ItemDto updItem(int userId, int itemId, ItemDto itemDtoToAdd);

    public ItemDto getItemById(int itemId);

    public List<ItemDto> getItemsByUser(int userId);

    public List<ItemDto> searchItems(String text);

    public CommentDto addComment(int itemId, int userId, Comment comment);
}
