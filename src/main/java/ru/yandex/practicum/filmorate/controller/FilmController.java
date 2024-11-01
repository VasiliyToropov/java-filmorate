package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j

public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmStorage.getFilm(id);
    }

    @GetMapping("/films/popular?count={count}")
    public Set<Film> getMostLikedFilms(@RequestParam Long count) {
        return filmService.getMostLikedFilms(count);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping
    public void deleteFilm(@RequestBody Film film) {
        filmStorage.deleteFilm(film);
    }

    @DeleteMapping("films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFilmNotFound(final NotFoundException e) {
        return Map.of("error", "Фильм с таким id не найден");
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
