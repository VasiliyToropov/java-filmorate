package ru.yandex.practicum.filmorate.jdbcTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenresForFilmsMapper;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.services.FilmValidator;
import ru.yandex.practicum.filmorate.storage.DalFilmStorage;
import ru.yandex.practicum.filmorate.storage.DalGenreStorage;
import ru.yandex.practicum.filmorate.storage.DalRatingStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DalFilmStorage.class, FilmRepository.class, FilmRowMapper.class, RatingRowMapper.class, FilmValidator.class,
        DalRatingStorage.class, RatingRepository.class, DalGenreStorage.class, GenreRepository.class,
        GenreRowMapper.class, GenresForFilmsMapper.class})

public class DalFilmStorageTests {

    private final DalFilmStorage dalFilmStorage;

    @BeforeEach
    public void beforeEach() {
        Film film = new Film();
        LocalDate date = LocalDate.parse("1990-12-02");
        Rating rating = new Rating();
        rating.setId(3);

        film.setName("Some Film");
        film.setDescription("Some Desc");
        film.setDuration(100L);
        film.setReleaseDate(date);
        film.setMpa(rating);

        dalFilmStorage.addFilm(film);
    }

    @Test
    public void testGetFilmById() {
        Film testFilm = dalFilmStorage.getFilm(1L);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id",1L);
        assertThat(testFilm).hasFieldOrPropertyWithValue("name","Some Film");
    }

    @Test
    public void testGetAllFilms() {
        Film film = new Film();
        LocalDate date = LocalDate.parse("1990-10-02");
        Rating rating = new Rating();
        rating.setId(3);

        film.setName("New Film");
        film.setDescription("New Desc");
        film.setDuration(100L);
        film.setReleaseDate(date);
        film.setMpa(rating);

        dalFilmStorage.addFilm(film);

        List<Film> films = dalFilmStorage.getAllFilms();

        assertThat(films).hasOnlyElementsOfType(Film.class);
        assertThat(films.getFirst()).hasFieldOrPropertyWithValue("id",1L);
        assertThat(films.getLast()).hasFieldOrPropertyWithValue("id",2L);
    }

    @Test
    public void testCreateFilm() {
        Film film = new Film();
        LocalDate date = LocalDate.parse("1990-10-02");
        Rating rating = new Rating();
        rating.setId(3);

        film.setName("New Film");
        film.setDescription("New Desc");
        film.setDuration(100L);
        film.setReleaseDate(date);
        film.setMpa(rating);

        dalFilmStorage.addFilm(film);

        Film testFilm = dalFilmStorage.getFilm(2L);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id",2L);
        assertThat(testFilm).hasFieldOrPropertyWithValue("name","New Film");
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film();
        LocalDate date = LocalDate.parse("1990-10-02");
        Rating rating = new Rating();
        rating.setId(3);

        film.setName("New Film");
        film.setDescription("New Desc");
        film.setDuration(100L);
        film.setReleaseDate(date);
        film.setMpa(rating);

        dalFilmStorage.addFilm(film);

        Film testFilm = dalFilmStorage.getFilm(2L);

        assertThat(testFilm).hasFieldOrPropertyWithValue("id",2L);

        dalFilmStorage.deleteFilm(testFilm);

        List<Film> users = dalFilmStorage.getAllFilms();

        assertThat(users).doesNotContain(testFilm);
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film();
        LocalDate date = LocalDate.parse("1990-10-02");
        Rating rating = new Rating();
        rating.setId(3);

        film.setName("New Film");
        film.setDescription("New Desc");
        film.setDuration(100L);
        film.setReleaseDate(date);
        film.setMpa(rating);
        film.setId(1L);

        dalFilmStorage.updateFilm(film);

        Film testFilm = dalFilmStorage.getFilm(1L);

        assertThat(testFilm).hasFieldOrPropertyWithValue("name","New Film");
        assertThat(testFilm).hasFieldOrPropertyWithValue("description","New Desc");
    }
}
