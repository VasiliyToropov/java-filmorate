package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTests {

    static FilmController filmController = new FilmController();

    //Проверяем прохождение валидации
    @Test
    void checkValidationOK() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(Duration.of(90, MINUTES));

        filmController.validate(validFilm);
    }

    //Проверяем неверную дату релиза
    @Test
    void checkValidationWithWrongReleaseDate() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1800-10-14"));
        validFilm.setDuration(Duration.of(90, MINUTES));

        assertThrows(ValidationException.class, () -> {
            filmController.validate(validFilm);
        });
    }

    //Проверяем валидацию, когда описание больше 200 символов
    @Test
    void checkValidationWhenDescriptionIsOver200Symbols() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Some Description. Some Description. Some Description. Some Description. " +
                "Some Description.Some Description.Some Description.Some Description.Some Description." +
                "Some Description.Some Description.Some Description.Some Description.Some Description.");
        validFilm.setReleaseDate(LocalDate.parse("1800-10-14"));
        validFilm.setDuration(Duration.of(90, MINUTES));

        assertThrows(ValidationException.class,() -> {
            filmController.validate(validFilm);
        });
    }

    //Проверяем валидацию, когда поля Имя - пустое
    @Test
    void checkValidationWhenFieldNameIsEmpty() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(Duration.of(90, MINUTES));

        assertThrows(ValidationException.class,() -> {
            filmController.validate(validFilm);
        });
    }

    //Проверяем валидацию, когда продолжительность отрицательное число
    @Test
    void checkValidationWhenDurationIsNegative() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(Duration.of(-90, MINUTES));

        assertThrows(ValidationException.class,() -> {
            filmController.validate(validFilm);
        });
    }
}
