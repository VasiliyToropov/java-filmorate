package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private Long id;

    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может быть свыше 200 символов")
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Дата релиза не может быть Null")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительным значением")
    private Long duration;

    private List<Genre> genres;

    private Rating mpa;
}
