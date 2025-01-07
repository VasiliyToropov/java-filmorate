package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenresForFilmsMapper;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresForFilms;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.services.FilmValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmRepository {
    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;
    private final RatingRowMapper ratingRowMapper;
    private final FilmValidator validator;
    private final GenresForFilmsMapper genresForFilmsMapper;
    private final GenreRowMapper genreRowMapper;

    public List<Film> getAllFilms() {
        String query = "SELECT * FROM films";
        return jdbc.query(query, mapper);
    }

    public Optional<Film> getFilm(Long id) {
        String query = "SELECT * FROM films WHERE id = ?";
        try {
            Film film = jdbc.queryForObject(query, mapper, id);

            //Добавляем рейтинг из БД
            String queryForMpa = "SELECT * FROM ratings WHERE rating_id = ?";
            Rating rating = jdbc.queryForObject(queryForMpa, ratingRowMapper, film.getMpa().getId());
            film.setMpa(rating);

            //Выгрузим из БД все жанры
            String queryForGenres = "SELECT * FROM genres";
            List<Genre> genres = jdbc.query(queryForGenres, genreRowMapper);

            //Добавляем в film жанры из БД
            String queryForGenresForFilms = "SELECT * FROM genresForFilms WHERE film_id = ?";
            List<GenresForFilms> genresForFilms = jdbc.query(queryForGenresForFilms, genresForFilmsMapper, film.getId());
            List<Genre> addGenresForFilm = new ArrayList<>();

            for (GenresForFilms forFilms : genresForFilms) {
                Long genreId = forFilms.getGenreId();
                for (Genre genre : genres) {
                    if (genre.getId().equals(genreId)) {
                        addGenresForFilm.add(genre);
                    }
                }
            }

            film.setGenres(addGenresForFilm);

            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public void deleteFilm(long id) {
        String query = "DELETE FROM films WHERE id = ?";
        jdbc.update(query, id);
    }

    public Film updateFilm(Film newFilm) {
        String query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";

        Optional<Film> film = getFilm(newFilm.getId());

        if (film.isEmpty()) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }


        jdbc.update(query, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(),
                newFilm.getDuration(), newFilm.getMpa().getId(), newFilm.getId());

        log.info("Фильм с ID: " + newFilm.getId() + " обновлен");

        return newFilm;
    }

    public Film addFilm(Film film) {

        validator.validate(film);
        validator.validateMpaId(film);
        validator.validateGenreId(film);

        String query = "INSERT into films(name, description, duration, release_date, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setLong(3, film.getDuration());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        //Добавляем ID жанров в таблицу genresForFilms
        List<Genre> filmGenres = film.getGenres();

        if (filmGenres == null) {
            return film;
        }

        //Удаляем дубликаты
        List<Genre> filmGenresWithoutDublicates;
        filmGenresWithoutDublicates = filmGenres.stream().distinct().collect(Collectors.toList());

        for (int i = 0; i < filmGenresWithoutDublicates.size(); i++) {
            String queryForGenres = "INSERT into genresForFilms(film_id, genre_id) VALUES (?, ?)";
            KeyHolder keyHolderForGenres = new GeneratedKeyHolder();
            int iter = i;
            jdbc.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(queryForGenres, new String[]{"row_id"});
                stmt.setLong(1, film.getId());
                stmt.setLong(2, film.getGenres().get(iter).getId());
                return stmt;
            }, keyHolderForGenres);
        }

        log.info("Фильм с ID: " + film.getId() + " создан");

        return film;
    }
}
