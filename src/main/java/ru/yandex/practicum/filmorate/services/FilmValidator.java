package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.Validator;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class FilmValidator implements Validator<Film> {

    public static final LocalDate CINEMA_BIRTH_DAY = LocalDate.parse("1895-12-28");

    public void validate(Film film) {
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        // дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate().isBefore(CINEMA_BIRTH_DAY)) {
            throw new ValidationException("Ошибка валидации - дата релиза раньше 1895-12-28");
        }
    }
}
