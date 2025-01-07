package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, mapper);
    }

    public Optional<User> getUser(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbc.queryForObject(query, mapper, id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public void deleteUser(long id) {
        String query = "DELETE FROM users WHERE id = ?";

        Optional<User> user = getUser(id);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }

        jdbc.update(query, id);
    }

    public User updateUser(User newUser) {
        String query = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";

        Optional<User> user = getUser(newUser.getId());

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }

        jdbc.update(query, newUser.getName(), newUser.getLogin(), newUser.getEmail(), newUser.getBirthday(), newUser.getId());

        return newUser;
    }

    public User createUser(User user) {
        String query = "INSERT into users(name,login,email,birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        log.info("Cоздан пользователь " + user.getId());

        return user;
    }
}
