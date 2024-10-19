package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.trace("Получили всех пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        // формируем дополнительные данные
        user.setId(id);

        // инкрементируем id для следующего пользователя
        id++;

        // если имя пустое, то используем логин
        ifNameIsNullOrEmpty(user);

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);

        log.trace("Пользователь добавлен");

        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.warn("Произошла ошибка валидации");
            throw new ValidationException("Ошибка валидации - Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            // если пользователь найден и все условия соблюдены, обновляем его содержимое
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());

            // если имя пустое, то используем логин
            ifNameIsNullOrEmpty(newUser, oldUser);

            oldUser.setBirthday(newUser.getBirthday());

            log.trace("Пользователь обновлен");

            return oldUser;
        }
        log.warn("Пользователь не найден");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public void ifNameIsNullOrEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void ifNameIsNullOrEmpty(User newUser, User oldUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            oldUser.setName(newUser.getLogin());
        } else {
            oldUser.setName(newUser.getName());
        }
    }
}
