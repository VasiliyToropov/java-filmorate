package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.MostLikedFilmsMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MostLikedFilms;
import ru.yandex.practicum.filmorate.storage.DalFilmStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class DalFilmService {

    private final JdbcTemplate jdbc;
    private final MostLikedFilmsMapper mapper;
    private final DalFilmStorage dalFilmStorage;

    public List<Film> getMostLikedFilms(Long count) {
        String query = "SELECT film_id, COUNT(*) AS num_likes " +
                "FROM likes " +
                "GROUP BY film_id " +
                "ORDER BY num_likes DESC " +
                "LIMIT ?";

        List<MostLikedFilms> mostLikedFilms = jdbc.query(query, mapper, count);
        List<Film> mostPopularFilms = new ArrayList<>();

        for (MostLikedFilms likedFilms : mostLikedFilms) {
            Film film = dalFilmStorage.getFilm(likedFilms.getFilmId());
            mostPopularFilms.add(film);
        }
        log.info("Получен список популярных фильмов");

        return mostPopularFilms;
    }

    public void addLike(Long filmId, Long userId) {
        String query = "INSERT into likes(user_id, film_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"row_id"});
            stmt.setLong(1, userId);
            stmt.setLong(2, filmId);
            return stmt;
        }, keyHolder);

        log.info("Пользователь " + userId + " поставил лайк фильму " + filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        String query = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

        jdbc.update(query, userId, filmId);

        log.info("Пользователь " + userId + " удалил лайк из фильма " + filmId);
    }
}
