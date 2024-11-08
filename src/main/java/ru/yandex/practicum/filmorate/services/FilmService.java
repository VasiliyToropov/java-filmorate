package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long id, Long userId) {

        if (id == null || userId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        Film film = filmStorage.getFilm(id);

        if (film == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        }

        User user = userStorage.getUser(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        film.getWhoLiked().add(userId);

        log.info("Лайк добавлен");
    }

    public void deleteLike(Long id, Long userId) {
        if (id == null || userId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        Film film = filmStorage.getFilm(id);

        if (film == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        }

        User user = userStorage.getUser(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }

        film.getWhoLiked().remove(userId);

        log.info("Лайк удален");
    }

    public List<Film> getMostLikedFilms(Long count) {
        List<Film> mostLikedFilms = new ArrayList<>();

        ArrayList<Film> filmsSortedByNumberOfLikes = filmStorage.getAllFilms();

        //Сортируем массив
        filmsSortedByNumberOfLikes.sort((f1, f2) -> {
            int count1 = f1.getWhoLiked().size();
            int count2 = f2.getWhoLiked().size();

            return count2 - count1;
        });

        if (count == null || count <= 0) {
            count = 10L;
        }

        if (count > filmsSortedByNumberOfLikes.size()) {
            count = (long) filmsSortedByNumberOfLikes.size();
        }

        for (int i = 0; i < count; i++) {
            mostLikedFilms.add(filmsSortedByNumberOfLikes.get(i));
        }

        log.info("Вернули список фильмов с самыми большими количеством лайков");

        return mostLikedFilms;
    }

}
