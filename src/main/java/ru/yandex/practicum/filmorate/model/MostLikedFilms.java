package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MostLikedFilms {
    private Long filmId;
    private Long numLikes;
}
