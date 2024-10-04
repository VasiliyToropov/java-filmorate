package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTests {

    static Validator validator = new Validator();

    //Проверяем прохождение валидации
    @Test
    void checkValidationOK() {
        Film validFilm = new Film();
        validFilm.setId(0L);
        validFilm.setName("Matrix");
        validFilm.setDescription("Matrix.Part I");
        validFilm.setReleaseDate(LocalDate.parse("1999-10-14"));
        validFilm.setDuration(100L);

        validator.validate(validFilm);
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
            validator.validate(validFilm);
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

        assertThrows(ValidationException.class, () -> {
            validator.validate(validFilm);
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
        validFilm.setDuration(100L);

        assertThrows(ValidationException.class, () -> {
            validator.validate(validFilm);
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
        validFilm.setDuration(-100L);

        assertThrows(ValidationException.class, () -> {
            validator.validate(validFilm);
        });
    }
}
