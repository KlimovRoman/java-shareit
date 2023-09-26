package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService; //поле куда будет передан сервис через контструктор с помощью зависимостей

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId, @Validated(Create.class) @RequestBody ItemDto newItem) {
        return itemService.addItem(userId,newItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updItem(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId, @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return itemService.updItem(userId,itemId,itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Запрошен поиск вещей по слову  {}", text);
        return itemService.searchItems(text);
    }
}
