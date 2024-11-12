package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;


    @Override
    public User createUser(User user) {
        // формируем дополнительные данные
        user.setId(id);

        // инкрементируем id для следующего пользователя
        id++;

        // если имя пустое, то используем логин
        ifNameIsNullOrEmpty(user);

        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);

        log.info("Пользователь добавлен");

        return user;
    }

    @Override
    public User getUser(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new NotFoundException("Пользователь с таким ID не найден");
    }

    @Override
    public void deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            log.info("Пользователь удален");
        } else throw new NotFoundException("Пользователь с таким ID не найден");
    }

    @Override
    public User updateUser(User newUser) {
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

            log.info("Пользователь обновлен");

            return oldUser;
        }
        log.warn("Пользователь не найден");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получили всех пользователей");
        return users.values().stream().toList();
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
