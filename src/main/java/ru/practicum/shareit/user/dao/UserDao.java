package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    public User addUser(User userToAdd);

    public User updUser(User userToUpd);

    public Optional<User> getUserById(int id);

    public List<User> getAllUsers();

    public User delUserById(int id);
}
