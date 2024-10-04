package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {
    public void validate(User user) {
        // электронная почта не может быть пустой и должна содержать символ @
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Ошибка валидации - эл.почта не может быть пустой и должна содержать @");
        }

        // логин не может быть пустым и содержать пробелы
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Ошибка валидации - логин не может быть пустым и содержать пробелы");
        }

        // дата рождения не может быть в будущем
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Ошибка валидации - дата рождения не может быть в будущем");
        }
    }

    public void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Ошибка валидации - имя не может быть пустым");
        }

        //максимальная длина описания — 200 символов
        if (film.getDescription().length() > 200 || film.getDescription().isBlank()) {
            throw new ValidationException("Ошибка валидации - длина описания больше 200 символов или пустое");
        }

        // дата релиза — не раньше 28 декабря 1895 года;
        LocalDate cinemaBirthDay = LocalDate.parse("1895-12-28");
        if (film.getReleaseDate().isBefore(cinemaBirthDay)) {
            throw new ValidationException("Ошибка валидации - дата релиза раньше 1895-12-28");
        }

        // продолжительность фильма должна быть положительным числом
        if (film.getDuration() <= 0) {
            throw new ValidationException("Ошибка валидации - продолжительность должна быть положительным числом");
        }
    }
}
