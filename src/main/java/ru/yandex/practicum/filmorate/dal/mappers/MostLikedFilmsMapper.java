package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MostLikedFilms;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MostLikedFilmsMapper implements RowMapper<MostLikedFilms> {

    @Override
    public MostLikedFilms mapRow(ResultSet rs, int rowNum) throws SQLException {
        MostLikedFilms mostLikedFilms = new MostLikedFilms();
        mostLikedFilms.setFilmId(rs.getLong("film_id"));
        mostLikedFilms.setNumLikes(rs.getLong("num_likes"));
        return mostLikedFilms;
    }
}
