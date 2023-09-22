package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService; //поле куда будет передан сервис через контструктор с помощью зависимостей


    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userToAdd) {
        return userService.addUser(userToAdd);
    }

    @PatchMapping("/{id}")
    public UserDto updUser(@PathVariable int id, @RequestBody UserDto userToUpd) {
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
    public UserDto delUserById(@PathVariable int id) {
        return userService.delUserById(id);
    }
}
