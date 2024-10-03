package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTests {
    static UserController userController = new UserController();

    //Проверяем прохождение валидации
    @Test
    void checkValidationOK() {
        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        userController.validate(validUser);
    }


    //Электронная почта должна содержать символ @
    @Test
    void checkValidationEmail() {
        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("userya.ru");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        assertThrows(ValidationException.class, () -> {
            userController.validate(validUser);
        });
    }

    //Электронная почта не должна быть пустой
    @Test
    void checkValidationEmailIsBlanc() {
        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        assertThrows(ValidationException.class, () -> {
            userController.validate(validUser);
        });
    }

    //Логин не может быть пустым
    @Test
    void checkValidationLoginIsBlanc() {
        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        assertThrows(ValidationException.class, () -> {
            userController.validate(validUser);
        });
    }

    //Логин не должен содержать пробелы
    @Test
    void checkValidationLoginHaveSpace() {
        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("user Login");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("1990-11-29"));

        assertThrows(ValidationException.class, () -> {
            userController.validate(validUser);
        });
    }

    //Дата рождения не должна быть в будущем
    @Test
    void checkValidationBirthdayInTheFuture() {
        User validUser = new User();

        validUser.setId(0L);
        validUser.setEmail("user@ya.ru");
        validUser.setLogin("userLogin");
        validUser.setName("userName");
        validUser.setBirthday(LocalDate.parse("2990-11-29"));

        assertThrows(ValidationException.class, () -> {
            userController.validate(validUser);
        });
    }
}



