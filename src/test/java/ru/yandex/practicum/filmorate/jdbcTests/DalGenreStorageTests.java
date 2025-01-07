package ru.yandex.practicum.filmorate.jdbcTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DalGenreStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreRowMapper.class, DalGenreStorage.class})

public class DalGenreStorageTests {

    private final DalGenreStorage dalGenreStorage;

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = dalGenreStorage.getAllGenres();

        assertThat(genres).hasOnlyElementsOfType(Genre.class);
        assertThat(genres.getFirst()).hasFieldOrPropertyWithValue("id",1L);
        assertThat(genres.getLast()).hasFieldOrPropertyWithValue("id",6L);
    }

    @Test
    public void testGetGenre() {
        Genre genre = dalGenreStorage.getGenre(1L);

        assertThat(genre).hasFieldOrPropertyWithValue("name","Комедия");
    }
}
