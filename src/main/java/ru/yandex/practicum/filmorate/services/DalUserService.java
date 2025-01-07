package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.mappers.FriendsRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DalUserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbc;
    private final FriendsRowMapper friendsRowMapper;

    public DalUserService(@Qualifier("DalUserStorage") UserStorage userStorage, JdbcTemplate jdbc, FriendsRowMapper friendsRowMapper) {
        this.userStorage = userStorage;
        this.jdbc = jdbc;
        this.friendsRowMapper = friendsRowMapper;
    }

    public User addFriend(Long id, Long friendId) {
        if (id == null || friendId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);
        User friendToUser = userStorage.getUser(friendId);

        log.info("Пробуем добавить пользователя " + id + " в друзья к пользователю " + friendId);
        //Добавляем запись в БД

        String query = "INSERT into friends(user_id,friend_id,friendshipStatus) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"row_id"});
            stmt.setLong(1, user.getId());
            stmt.setLong(2, friendToUser.getId());
            stmt.setString(3, "CHECKED");
            return stmt;
        }, keyHolder);

        log.info("Пользователь " + id + " добавил в друзья пользователя " + friendId);

        return user;
    }

    public void deleteFriend(Long id, Long friend_id) {
        if (id == null || friend_id == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }
        User user = userStorage.getUser(id);
        User friendToUser = userStorage.getUser(friend_id);

        if (user == null & friendToUser == null) {
            throw new NotFoundException("Пользователь с такими id не найдены");
        }

        String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

        jdbc.update(query, id, friend_id);

        log.info("Пользователь " + id + " удалил из друзей " + friend_id);
    }

    public List<User> getFriends(Long id) {
        if (id == null) {
            throw new NotFoundException("Пользователь с такими id не найдены");
        }

        // Проверяем, если такой id в БД
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с такими id не найдены");
        }

        List<Friendship> friendship;
        List<User> friendsToUser = new ArrayList<>();

        String query = "SELECT * FROM friends WHERE user_id = ?";

        friendship = jdbc.query(query, friendsRowMapper, id);

        for (Friendship friendId : friendship) {
            friendsToUser.add(userStorage.getUser(friendId.getFriendId()));
        }

        return friendsToUser;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        if (id == null || otherId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);

        // Проверяем, если такие id в БД
        if (user == null & otherUser == null) {
            throw new NotFoundException("Пользователь с такими id не найдены");
        }

        System.out.println(id + " " + otherId);

        List<Friendship> user1friends;
        List<Friendship> user2friends;

        String queryForUser1 = "SELECT * FROM friends WHERE user_id = ?";
        user1friends = jdbc.query(queryForUser1, friendsRowMapper, id);

        String queryForUser2 = "SELECT * FROM friends WHERE user_id = ?";
        user2friends = jdbc.query(queryForUser2, friendsRowMapper, otherId);

        Set<Long> friendsIdsForUser1 = new HashSet<>();
        Set<Long> friendsIdsForUser2 = new HashSet<>();

        for (Friendship row : user1friends) {
            friendsIdsForUser1.add(row.getFriendId());
        }

        for (Friendship row : user2friends) {
            friendsIdsForUser2.add(row.getFriendId());
        }

        List<User> commonFriends = new ArrayList<>();

        for (Long i : friendsIdsForUser1) {
            if (friendsIdsForUser2.contains(i)) {
                commonFriends.add(userStorage.getUser(i));
            }
        }

        log.info("Получен список друзей, общих с другим пользователем");

        return commonFriends;
    }
}
