package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.GenresForFilms;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenresForFilmsMapper implements RowMapper<GenresForFilms> {

    @Override
    public GenresForFilms mapRow(ResultSet rs, int rowNum) throws SQLException {
        GenresForFilms genresForFilms = new GenresForFilms();
        genresForFilms.setRowId(rs.getLong("row_id"));
        genresForFilms.setGenreId(rs.getLong("genre_id"));
        genresForFilms.setFilmId(rs.getLong("film_id"));

        return genresForFilms;
    }
}
