package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmValidator validator = new FilmValidator();
    private long id = 1;


    @Override
    public Film addFilm(@Valid Film film) {
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
    @Override
    public Film getFilm(Long id) {
        if(films.containsKey(id)) {
            return films.get(id);
        } else throw new NotFoundException("Фильм с таким ID не найден");
    }

    @Override
    public void deleteFilm(@Valid Film film) {
        if(films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.trace("Фильм удален");
        } else throw new NotFoundException("Фильм с таким ID не найден");
    }

    @Override
    public Film updateFilm(@Valid Film newFilm) {
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

    @Override
    public List<Film> getAllFilms() {
        log.trace("Получили все фильмы");
        return films.values().stream().toList();
    }


}
