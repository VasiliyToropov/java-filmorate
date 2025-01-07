package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DalGenreStorage {

    private final GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public Genre getGenre(Long id) {
        Optional<Genre> result = genreRepository.getGenre(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new NotFoundException("Жанр с таким ID не найден");
    }
}
