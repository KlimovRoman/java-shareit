package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Component
public class ItemInMemDao implements ItemDao {
    private Map<Integer, Item> items = new HashMap<>(); //хранилище вещей
    private int itemIdCounter = 0;

    @Override
    public Item addItem(Item item) {
        itemIdCounter++;
        item.setId(itemIdCounter);
        items.put(itemIdCounter, item);
        log.info("Добавлена вещь с id = {}", itemIdCounter);
        return item;
    }

    @Override
    public Optional<Item> getItem(int itemId) {
        Item item = items.get(itemId);
        if (item != null) {
            log.info("Запрошена вещь с id = {}", itemId);
            return Optional.ofNullable(item);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Item updItem(ItemDto itemDto, int itemId) {
        Item itemToUpd = items.get(itemId);
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            itemToUpd.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            itemToUpd.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemToUpd.setAvailable(itemDto.getAvailable());
        }
        log.info("Обновлена вещь с id = {}", itemId);
        return itemToUpd;
    }

    @Override
    public List<ItemDto> getItemsByUser(int userId) {
        List<ItemDto> itemsForUser = new ArrayList<>();
        for (Item item: items.values()) {
            if (item.getOwner().getId() == userId) {
                itemsForUser.add(ItemMapper.itemToDto(item));
            }
        }
        log.info("Запрошен список вещей пользователя с  id = {}", userId);
        return itemsForUser;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> itemsForUserSearch = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(text)) {
                    itemsForUserSearch.add(ItemMapper.itemToDto(item));
                } else if (item.getDescription().toLowerCase().contains(text)) {
                    itemsForUserSearch.add(ItemMapper.itemToDto(item));
                }
            }
        }
        log.info("Запрошен поиск вещей по слову  {}", text);
        return itemsForUserSearch;
    }
}