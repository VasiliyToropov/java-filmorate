package ru.yandex.practicum.filmorate.jdbcTests;

import lombok.RequiredArgsConstructor;
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

    @Test
    public void testGetFilmById() {
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

        Film testFilm = dalFilmStorage.getFilm(film.getId());

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


        Film film2 = new Film();
        LocalDate date2 = LocalDate.parse("1990-12-02");
        Rating rating2 = new Rating();
        rating2.setId(3);

        film2.setName("Some Film");
        film2.setDescription("Some Desc");
        film2.setDuration(100L);
        film2.setReleaseDate(date2);
        film2.setMpa(rating2);

        dalFilmStorage.addFilm(film2);

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

        Film testFilm = dalFilmStorage.getFilm(film.getId());

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

        Film testFilm = dalFilmStorage.getFilm(film.getId());

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

        dalFilmStorage.addFilm(film);

        Film updateFilm = new Film();
        LocalDate updateDate = LocalDate.parse("1990-10-02");
        Rating updateRating = new Rating();
        updateRating.setId(3);

        updateFilm.setName("New Film");
        updateFilm.setDescription("New Desc");
        updateFilm.setDuration(100L);
        updateFilm.setReleaseDate(updateDate);
        updateFilm.setMpa(updateRating);
        updateFilm.setId(film.getId());

        dalFilmStorage.updateFilm(updateFilm);

        Film testFilm = dalFilmStorage.getFilm(updateFilm.getId());

        assertThat(testFilm).hasFieldOrPropertyWithValue("name","New Film");
        assertThat(testFilm).hasFieldOrPropertyWithValue("description","New Desc");
    }
}
