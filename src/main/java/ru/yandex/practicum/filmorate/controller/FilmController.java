package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.trace("Получили все фильмы");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        // проверяем выполнение необходимых условий
        validate(film);
        // формируем дополнительные данные
        film.setId(getNextId());

        // сохраняем новый фильм в памяти приложения
        films.put(film.getId(), film);
        log.trace("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.warn("Произошла ошибка валидации");
            throw new ValidationException("Ошибка валидации - Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            validate(newFilm);

            // если фильм найден и все условия соблюдены, обновляем его содержимое
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());

            log.trace("Фильм обновлен");
            return oldFilm;
        }
        log.warn("Фильм не найден");
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }


    // вспомогательный метод для генерации идентификатора нового фильма
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    // метод для проверки условий валидации
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
