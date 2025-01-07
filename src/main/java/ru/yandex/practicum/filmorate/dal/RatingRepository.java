package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RatingRepository {
    private final JdbcTemplate jdbc;
    private final RatingRowMapper ratingRowMapper;

    public List<Rating> getAllRatings() {
        String query = "SELECT * FROM ratings";
        return jdbc.query(query, ratingRowMapper);
    }

    public Optional<Rating> getRating(Long id) {
        String query = "SELECT * FROM ratings WHERE rating_id = ?";

        try {
            Rating rating = jdbc.queryForObject(query, ratingRowMapper, id);
            return Optional.ofNullable(rating);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}
