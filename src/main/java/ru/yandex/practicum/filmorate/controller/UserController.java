package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.Validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final Validator validator = new Validator();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.trace("Получили всех пользователей");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        validator.validate(user);

        // формируем дополнительные данные
        user.setId(getNextId());

        // если имя пустое, то используем логин
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);

        log.trace("Пользователь добавлен");

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            log.warn("Произошла ошибка валидации");
            throw new ValidationException("Ошибка валидации - Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            validator.validate(newUser);

            // если пользователь найден и все условия соблюдены, обновляем его содержимое
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());

            if (newUser.getName() == null) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }

            oldUser.setBirthday(newUser.getBirthday());

            log.trace("Пользователь обновлен");

            return oldUser;
        }
        log.warn("Пользователь не найден");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
