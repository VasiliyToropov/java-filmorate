package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User getUser(Long id);
    public User createUser(User user);
    public void deleteUser(User user);
    public User updateUser(User newUser);
    public List<User> getAllUsers();
}
