package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;

@SpringBootApplication
@Slf4j
public class FilmorateApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FilmorateApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        UserController userController = context.getBean(UserController.class);
        FilmService filmService = context.getBean(FilmService.class);
        FilmController filmController = context.getBean(FilmController.class);
        log.info("Приложение Filmorate запущено");
    }

}
