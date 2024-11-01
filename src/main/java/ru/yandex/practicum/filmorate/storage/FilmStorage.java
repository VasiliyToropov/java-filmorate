package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film getFilm(Long id);
    public Film addFilm(Film film);
    public void deleteFilm(Film film);
    public Film updateFilm(Film newFilm);
    public List<Film> getAllFilms();
}
