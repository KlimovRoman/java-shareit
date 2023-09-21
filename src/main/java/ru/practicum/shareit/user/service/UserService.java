package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    public UserDto addUser(UserDto userDtoToAdd);

    public UserDto updUser(int id, UserDto userToUpd);

    public List<UserDto> getAllUsers();

    public UserDto getUserById(int id);

    public UserDto delUserById(int id);
}
