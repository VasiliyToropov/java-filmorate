package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserControllerTests {

    private Validator validator;

    //Проверяем прохождение валидации
    @Test
    void checkValidationOK() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertTrue(violations.isEmpty());
    }


    //Электронная почта должна содержать символ @
    @Test
    void checkValidationEmail() {
        User validUser = new User();

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validUser.setId(0L);
        validUser.setEmail("userya.ru");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
    }

    //Электронная почта не должна быть пустой
    @Test
    void checkValidationEmailIsBlanc() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
    }

    //Логин не может быть пустым
    @Test
    void checkValidationLoginIsBlanc() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
    }

    //Логин не должен содержать пробелы
    @Test
    void checkValidationLoginHaveSpace() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("user Login");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
    }

    //Дата рождения не должна быть в будущем
    @Test
    void checkValidationBirthdayInTheFuture() {

        ValidatorFactory factory = buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("2990-11-29"));

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertFalse(violations.isEmpty());
    }
}



