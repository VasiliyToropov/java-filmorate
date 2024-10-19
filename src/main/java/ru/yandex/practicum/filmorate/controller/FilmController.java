package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    private final FilmValidator validator = new FilmValidator();

    private long id = 1;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.trace("Получили все фильмы");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        // проверяем выполнение необходимых условий
        validator.validate(film);

        // формируем дополнительные данные
        film.setId(id);

        // сохраняем новый фильм в памяти приложения
        films.put(film.getId(), film);
        log.trace("Фильм добавлен");

        // инкрементируем id для следующего фильма
        id++;

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        // проверяем необходимые условия
        if (newFilm.getId() == null) {
            log.warn("Произошла ошибка валидации");
            throw new ValidationException("Ошибка валидации - Id должен быть указан");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            validator.validate(newFilm);

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
}
