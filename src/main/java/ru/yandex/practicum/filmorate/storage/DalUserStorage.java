package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Service("DalUserStorage")
@RequiredArgsConstructor
public class DalUserStorage implements UserStorage {

    private final UserRepository userRepository;

    @Override
    public User getUser(Long id) {
        Optional<User> result = userRepository.getUser(id);
        if (result.isPresent()) {
            return result.get();
        }
        throw new NotFoundException("Пользователь с таким ID не найден");
    }

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.deleteUser(user.getId());
    }

    @Override
    public User updateUser(User newUser) {
        return userRepository.updateUser(newUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
