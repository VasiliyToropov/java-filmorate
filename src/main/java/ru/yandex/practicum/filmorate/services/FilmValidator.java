package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.Validator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.DalGenreStorage;
import ru.yandex.practicum.filmorate.storage.DalRatingStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FilmValidator implements Validator<Film> {

    private final DalRatingStorage dalRatingStorage;
    private final DalGenreStorage dalGenreStorage;

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

    //Проверяем корректный mpa_id
    public void validateMpaId(Film film) {
        Long mpaId = film.getMpa().getId();

        List<Rating> ratings = dalRatingStorage.getAllRatings();

        for (Rating rating : ratings) {
            if (rating.getId() == mpaId) {
                return;
            }
        }
        throw new ValidationException("Id рейтинга не найден");
    }

    //Проверяем корректный genre_id
    public void validateGenreId(Film film) {

        List<Genre> filmGenres = film.getGenres();

        //Если id жанров не указаны, то просто выходим из метода.
        if (filmGenres == null) {
            return;
        }

        List<Genre> dbGenres = dalGenreStorage.getAllGenres();

        for (Genre filmGenre : filmGenres) {
            Long filmGenreId = filmGenre.getId();
            for (Genre dbGenre : dbGenres) {
                Long dbGenreId = dbGenre.getId();
                if (Objects.equals(filmGenreId, dbGenreId)) {
                    return;
                }
            }
        }

        throw new ValidationException("Id жанра не найден");
    }
}
