package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DalGenreStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final DalGenreStorage dalGenreStorage;

    @GetMapping
    public List<Genre> getAllGenres() {
        return dalGenreStorage.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable long id) {
        return dalGenreStorage.getGenre(id);
    }
}
