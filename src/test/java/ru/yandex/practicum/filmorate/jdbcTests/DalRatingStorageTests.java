package ru.yandex.practicum.filmorate.jdbcTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.RatingRepository;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.DalRatingStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({RatingRepository.class, RatingRowMapper.class, DalRatingStorage.class})
public class DalRatingStorageTests {

    private final DalRatingStorage dalRatingStorage;

    @Test
    public void testGetAllRatings() {
        List<Rating> ratings = dalRatingStorage.getAllRatings();

        assertThat(ratings).hasOnlyElementsOfType(Rating.class);
        assertThat(ratings.getFirst()).hasFieldOrPropertyWithValue("id",1L);
        assertThat(ratings.getLast()).hasFieldOrPropertyWithValue("id",5L);
    }

    @Test
    public void testGetGenre() {
        Rating rating = dalRatingStorage.getRating(1L);

        assertThat(rating).hasFieldOrPropertyWithValue("name","G");
    }

}
