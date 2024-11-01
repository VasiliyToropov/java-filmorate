package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private Long id;

    @Email(message = "Формат почты должен быть правильным")
    @NotEmpty(message = "Значение почты не может быть пустым")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^ ]+$", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем")
    @NotNull(message = "Дата рождения не может быть null")
    private LocalDate birthday;

    private Set<Long> friendsIds;
}
