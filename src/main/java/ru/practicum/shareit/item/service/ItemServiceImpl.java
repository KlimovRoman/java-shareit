package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.EntityNotFoundException;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserDao userDao; //внедрили репозиторий юзеров
    private final ItemDao itemDao; //внедрили репозиторий вещей

    @Override
    public ItemDto addItem(int userId, ItemDto itemDtoToAdd) {
        User user = userDao.getUserById(userId).orElseThrow(() -> new EntityNotFoundException("пользователь не найден!"));
        return ItemMapper.itemToDto(itemDao.addItem(ItemMapper.dtoToItem(itemDtoToAdd, user)));
    }

    @Override
    public ItemDto updItem(int userId, int itemId, ItemDto itemDtoToAdd) {
        Item item = itemDao.getItem(itemId).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
        if (userId !=  item.getOwner().getId()) {
            throw new EntityNotFoundException("Ошибка! Вещь может просматривать только автор!");
        }
        return ItemMapper.itemToDto(itemDao.updItem(itemDtoToAdd, itemId));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        Item item = itemDao.getItem(itemId).orElseThrow(() -> new EntityNotFoundException("Вещь не найдена!"));
        return ItemMapper.itemToDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUser(int userId) {
        return itemDao.getItemsByUser(userId);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text != null) {
            String text2 = text.toLowerCase();
            return itemDao.searchItems(text2);
        } else {
            throw new EntityNotFoundException("Строка поиска пуста!");
        }
    }
}
