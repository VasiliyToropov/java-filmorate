package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userStorage.getUser(id);
    }

    // Возврат списка пользователей
    @GetMapping("/users/{id}/friends")
    public Set<Long> getFriends(@PathVariable Long id) {
        return userService.getFriends(id);
    }

    //Возвращаем список друзей, общих с другим пользователем
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<Long> getFriendsSharedWithAnotherUser(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getFriendsSharedWithAnotherUser(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        return userStorage.updateUser(newUser);
    }

    //Добавление в друзья
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody User user) {
        userStorage.deleteUser(user);
    }

    //Удаление друга
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFound(final NotFoundException e) {
        return Map.of("error", "Пользователь с таким id не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFailedValidation(final ValidationException e) {
        return Map.of("error", "Произошла ошибка валидации");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleOtherExceptions(final Exception e) {
        return Map.of("error", "Возникло исключение");
    }
}