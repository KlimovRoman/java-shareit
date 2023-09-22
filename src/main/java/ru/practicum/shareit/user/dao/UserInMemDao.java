package ru.practicum.shareit.user.dao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeption.EntityNotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.user.model.User;
import java.util.*;

@Slf4j
@Component
public class UserInMemDao implements UserDao {
    private Map<Integer,User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>(); //список уникальных значений email
    private int userIdCounter = 0;

    @Override
    public User addUser(User userToAdd) {
        emailValidation(userToAdd);
        userIdCounter++;
        userToAdd.setId(userIdCounter);
        users.put(userIdCounter, userToAdd);
        log.info("Добавлен юзер с id = {}", userIdCounter);
        return userToAdd;
    }

    @Override
    public User updUser(User userToUpd) {
        final int id = userToUpd.getId();
        User user = users.get(id);
        if (user != null) {
            if (userToUpd.getEmail() != null) {
                if (!user.getEmail().equals(userToUpd.getEmail())) {
                    emailValidation(userToUpd);
                    String emailToDel = user.getEmail();
                    user.setEmail(userToUpd.getEmail());
                    emails.remove(emailToDel);
                }
            }
            if (userToUpd.getName() != null) {
                user.setName(userToUpd.getName());
            }
            log.info("Обновлен юзер с id = {}", id);
            return user;
        } else {
            log.info("пользователь не найден!");
            throw new EntityNotFoundException("пользователь не найден!");
        }
    }

    @Override
    public List<User> getAllUsers() {
          return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw  new EntityNotFoundException("Пользователь не найден!");
        } else {
            return Optional.ofNullable(user);
        }
    }


    @Override
    public User delUserById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw  new EntityNotFoundException("Пользователь не найден!");
        } else {
            emails.remove(users.get(id).getEmail());
            return users.remove(id);
        }
    }

    private void emailValidation(User user) {
        if (emails.contains(user.getEmail())) {
            throw  new ValidationException("Такой email уже существует!");
        } else {
            emails.add(user.getEmail());
        }
    }
}