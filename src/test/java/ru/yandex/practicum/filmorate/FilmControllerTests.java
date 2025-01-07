package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmControllerTests {

    private Validator validator;


    //Проверяем прохождение валидации
    @Test
    void checkValidationOK() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertTrue(violations.isEmpty());
    }

    //Проверяем валидацию, когда описание больше 200 символов
    @Test
    void checkValidationWhenDescriptionIsOver200Symbols() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Some Description. Some Description. Some Description. Some Description. " +
                "Some Description.Some Description.Some Description.Some Description.Some Description." +
                "Some Description.Some Description.Some Description.Some Description.Some Description.");
        validFilm.setReleaseDate(LocalDate.parse("1800-10-14"));
        validFilm.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertFalse(violations.isEmpty());
    }

    //Проверяем валидацию, когда поля Имя - пустое
    @Test
    void checkValidationWhenFieldNameIsEmpty() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertFalse(violations.isEmpty());
    }

    //Проверяем валидацию, когда продолжительность отрицательное число
    @Test
    void checkValidationWhenDurationIsNegative() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(-100L);

        Set<ConstraintViolation<Film>> violations = validator.validate(validFilm);
        assertFalse(violations.isEmpty());
    }
}
