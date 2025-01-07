package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class GenresForFilms {
    private Long rowId;
    private Long filmId;
    private Long genreId;
}
