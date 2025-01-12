package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Service("DalFilmStorage")
@RequiredArgsConstructor
public class DalFilmStorage implements FilmStorage {

    private final FilmRepository filmRepository;

    @Override
    public Film getFilm(Long id) {
        Optional<Film> result = filmRepository.getFilm(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new NotFoundException("Фильм с таким ID не найден");
    }

    @Override
    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    @Override
    public void deleteFilm(Film film) {
        filmRepository.deleteFilm(film.getId());
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return filmRepository.updateFilm(newFilm);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }
}
