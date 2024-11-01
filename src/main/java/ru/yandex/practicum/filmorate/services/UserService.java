package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

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
        user.getFriendsIds().add(friendId);

        log.info("Друг добавлен");

        return user;
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id == null || friendId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);

        user.getFriendsIds().remove(friendId);

        log.info("Друг удален");
    }

    public Set<Long> getFriends(Long id) {
        if (id == null) {
            throw new NotFoundException("Пользователь с такими id не найдены");
        }

        User user = userStorage.getUser(id);

        log.trace("Получен список друзей");

        return user.getFriendsIds();
    }

    public Set<Long> getFriendsSharedWithAnotherUser(Long id, Long otherId) {
        if (id == null || otherId == null) {
            throw new NotFoundException("Пользователи с такими id не найдены");
        }

        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);

        Set<Long> friendsSharedWithAnotherUser = new HashSet<>();

        Stream<Long> userFriendList = user.getFriendsIds().stream();
        Stream<Long> otherUserFriendList = otherUser.getFriendsIds().stream();

        userFriendList.filter(element -> otherUserFriendList.anyMatch(e -> Objects.equals(e, element)))
                .forEach(friendsSharedWithAnotherUser::add);

        log.trace("Получен список друзей , общих с другим пользователем");

        return friendsSharedWithAnotherUser;
    }
}
