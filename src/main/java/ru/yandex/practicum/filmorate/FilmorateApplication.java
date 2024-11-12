package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class FilmorateApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FilmorateApplication.class, args);
        log.info("Приложение Filmorate запущено");
    }
}
