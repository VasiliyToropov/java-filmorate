package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long id, Long friendId) {

        if (id == null || friendId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);
        User friendToUser = userStorage.getUser(friendId);

        user.getFriendsIds().add(friendId);
        friendToUser.getFriendsIds().add(id);

        log.info("Пользователь " + id + " добавил в друзья пользователя " + friendId);

        return user;
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id == null || friendId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);
        User friendToUser = userStorage.getUser(friendId);

        user.getFriendsIds().remove(friendId);
        friendToUser.getFriendsIds().remove(id);

        log.info("Друг удален");
    }

    public List<User> getFriends(Long id) {
        if (id == null) {
            throw new NotFoundException("Пользователь с такими id не найдены");
        }

        User user = userStorage.getUser(id);

        List<User> friendsToUser = new ArrayList<>();

        for (Long i : user.getFriendsIds()) {
            friendsToUser.add(userStorage.getUser(i));
        }

        log.info("Вернули список друзей");

        return friendsToUser;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        if (id == null || otherId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);

        Set<Long> commonFriendsIds = new HashSet<>();

        for (Long i : user.getFriendsIds()) {
            if (otherUser.getFriendsIds().contains(i)) {
                commonFriendsIds.add(i);
            }
        }

        List<User> commonFriends = new ArrayList<>();

        for (Long i : commonFriendsIds) {
            commonFriends.add(userStorage.getUser(i));
        }

        log.info("Получен список друзей, общих с другим пользователем");

        return commonFriends;
    }
}
