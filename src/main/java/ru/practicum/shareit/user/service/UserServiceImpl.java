package ru.practicum.shareit.user.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.EntityNotFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao; //поле куда будет передан репозиторий через контструктор с помощью зависимостей
    // связали зависимостью сервис и репозиторий через аннотацию RequiredArgsConstructor
    private final UserRepository userRepo; //JPA репозиторий


    @Override
    public UserDto addUser(UserDto userDtoToAdd) {
        //  ковертация в user, добавление user, получение в ответ user, конвертация в dto и передача в контроллер
        log.info("JPA - Добавление в БД User");
        return UserMapper.userToDto(userRepo.save(UserMapper.dtoToUser(userDtoToAdd)));
    }

    @Override
    public UserDto updUser(int id, UserDto userToUpd) {
        log.info("JPA - Обновление в БД User");
        User userFromDb = userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("пользователь не найден!"));
        if (userToUpd.getEmail() != null && !userToUpd.getEmail().isBlank()) {
            if (!userToUpd.getEmail().equals(userFromDb.getEmail())) {
                userFromDb.setEmail(userToUpd.getEmail());
            }
        }
        if (userToUpd.getName() != null && !userToUpd.getName().isBlank()) {
            userFromDb.setName(userToUpd.getName());
        }

        return UserMapper.userToDto(userRepo.save(userFromDb));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("JPA - getAllUsers");
        List<UserDto> usrsDto = new ArrayList<>();
        List<User> usrs = userRepo.findAll();
        for (User usr: usrs) {
            usrsDto.add(UserMapper.userToDto(usr));
        }
        return usrsDto;
    }

    @Override
    public UserDto getUserById(int id) {
        log.info("JPA - getUserById {}",id);
        return UserMapper.userToDto(
                userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("пользователь не найден!")));
    }

    @Override
    public void delUserById(int id) {
        log.info("JPA - delUserById {}",id);
        userRepo.deleteById(id);
    }
}
