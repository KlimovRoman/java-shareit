package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;


import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService; //поле куда будет передан сервис через контструктор с помощью зависимостей


    @PostMapping
    public UserDto addUser(@Validated(Create.class) @RequestBody UserDto userToAdd) {
        return userService.addUser(userToAdd);
    }

    @PatchMapping("/{id}")
    public UserDto updUser(@PathVariable int id, @Validated(Update.class) @RequestBody UserDto userToUpd) {
        return userService.updUser(id, userToUpd);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void delUserById(@PathVariable int id) {
         userService.delUserById(id);
    }
}
