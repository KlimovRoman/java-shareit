package ru.practicum.shareit.user.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.EntityNotFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao; //поле куда будет передан репозиторий через контструктор с помощью зависимостей
    // связали зависимостью контроллер и сервис через аннотацию RequiredArgsConstructor

    @Override
    public UserDto addUser(UserDto userDtoToAdd) {
        //  ковертация в user, добавление user, получение в ответ user, конвертация в dto и передача в контроллер
        return UserMapper.userToDto(userDao.addUser(UserMapper.dtoToUser(userDtoToAdd)));
    }

    @Override
    public UserDto updUser(int id, UserDto userToUpd) {
        userToUpd.setId(id);
        return UserMapper.userToDto(userDao.updUser(UserMapper.dtoToUser(userToUpd)));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usrsDto = new ArrayList<>();
        List<User> usrs = userDao.getAllUsers();
        for(User usr: usrs) {
            usrsDto.add(UserMapper.userToDto(usr));
        }
        return usrsDto;
    }

    @Override
    public UserDto getUserById(int id) {
        return UserMapper.userToDto(
                userDao.getUserById(id).orElseThrow(() -> new EntityNotFoundException("пользователь не найден!")));
    }

    @Override
    public UserDto delUserById(int id) {
        return UserMapper.userToDto(userDao.delUserById(id));
    }
}
