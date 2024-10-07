package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmValidator;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTests {

    static FilmValidator filmValidator = new FilmValidator();
    private Validator validator;
    @Before
    public void setUp() {
        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    //Проверяем прохождение валидации
    @Test
    void checkValidationOK() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(100L);

        filmValidator.validate(validFilm);
        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertTrue(violations.isEmpty());
    }

    //Проверяем неверную дату релиза
    @Test
    void checkValidationWithWrongReleaseDate() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1800-10-14"));
        validFilm.setDuration(100L);

        assertThrows(ValidationException.class, () -> {
            filmValidator.validate(validFilm);
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
        validFilm.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertTrue(violations.isEmpty());
    }

    //Проверяем валидацию, когда поля Имя - пустое
    @Test
    void checkValidationWhenFieldNameIsEmpty() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertTrue(violations.isEmpty());
    }

    //Проверяем валидацию, когда продолжительность отрицательное число
    @Test
    void checkValidationWhenDurationIsNegative() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(-100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertTrue(violations.isEmpty());
    }
}
