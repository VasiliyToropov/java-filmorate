package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long id, Long userId) {
        if(id == null || userId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        Film film = filmStorage.getFilm(id);

        if(film.getWhoLiked().contains(userId)) {
            log.warn("Пользователь уже лайкнул этот фильм");
        } else {
            film.getWhoLiked().add(userId);
            log.info("Лайк добавлен");
        }
    }

    public void deleteLike(Long id, Long userId) {
        if(id == null || userId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        Film film = filmStorage.getFilm(id);

        if(film.getWhoLiked().contains(userId)) {
            film.getWhoLiked().remove(userId);
            log.info("Лайк удален");
        } else {
            log.warn("Пользователь не лайкал этот фильм");
        }
    }

    public Set<Film> getMostLikedFilms(Long count) {
        Set<Film> mostLikedFilms = new HashSet<>();

        List<Film> filmsSortedByNumberOfLikes = filmStorage.getAllFilms();

        filmsSortedByNumberOfLikes.sort(new CountComparator());

        if(count == null || count <= 0) {
            count = 10L;
        }

        for (int i = 0; i < count; i++) {
            mostLikedFilms.add(filmsSortedByNumberOfLikes.get(i));
        }

        log.trace("Вернули список фильмов с самыми большими количеством лайков");

        return mostLikedFilms;
    }

}
