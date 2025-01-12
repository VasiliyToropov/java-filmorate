package ru.yandex.practicum.filmorate.jdbcTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DalUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DalUserStorage.class, UserRepository.class, UserRowMapper.class})

class DalUserStorageTests {
    private final DalUserStorage dalUserStorage;

    @Test
    public void testGetUserById() {
        User user = new User();
        LocalDate date = LocalDate.parse("1990-11-02");

        user.setName("Some Name");
        user.setLogin("Some Login");
        user.setEmail("Some Email");
        user.setBirthday(date);

        dalUserStorage.createUser(user);

        User userOptional = dalUserStorage.getUser(user.getId());

        assertThat(userOptional).hasFieldOrPropertyWithValue("name","Some Name");
    }

    @Test
    public void testGetAllUsers() {

        User user = new User();
        LocalDate date = LocalDate.parse("1990-11-02");

        user.setName("Some Name");
        user.setLogin("Some Login");
        user.setEmail("Some Email");
        user.setBirthday(date);

        dalUserStorage.createUser(user);

        User user2 = new User();
        LocalDate date2 = LocalDate.parse("1990-12-02");

        user2.setName("New Name");
        user2.setLogin("New Login");
        user2.setEmail("New EMail");
        user2.setBirthday(date2);

        dalUserStorage.createUser(user2);

        List<User> users = dalUserStorage.getAllUsers();

        assertThat(users).hasOnlyElementsOfType(User.class);
        assertThat(users.getFirst()).hasFieldOrPropertyWithValue("id",1L);
        assertThat(users.getLast()).hasFieldOrPropertyWithValue("id",2L);

    }

    @Test
    public void testCreateUser() {
        User user = new User();
        LocalDate date = LocalDate.parse("1990-12-02");

        user.setName("New Name");
        user.setLogin("New Login");
        user.setEmail("New EMail");
        user.setBirthday(date);

        dalUserStorage.createUser(user);

        User testUser = dalUserStorage.getUser(user.getId());

        assertThat(testUser).hasFieldOrPropertyWithValue("name","New Name");
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        LocalDate date = LocalDate.parse("1990-12-02");

        user.setName("New Name");
        user.setLogin("New Login");
        user.setEmail("New EMail");
        user.setBirthday(date);

        dalUserStorage.createUser(user);

        dalUserStorage.deleteUser(user);

        List<User> users = dalUserStorage.getAllUsers();

        assertThat(users).doesNotContain(user);
    }

    @Test
    public void testUpdateUser() {

        User user = new User();
        LocalDate date = LocalDate.parse("1990-11-02");

        user.setName("Some Name");
        user.setLogin("Some Login");
        user.setEmail("Some Email");
        user.setBirthday(date);

        dalUserStorage.createUser(user);

        User updateUser = new User();
        LocalDate updateDate = LocalDate.parse("1990-12-02");

        updateUser.setName("New Name");
        updateUser.setLogin("New Login");
        updateUser.setEmail("New EMail");
        updateUser.setBirthday(updateDate);
        updateUser.setId(user.getId());

        dalUserStorage.updateUser(updateUser);

        User testUser = dalUserStorage.getUser(updateUser.getId());

        assertThat(testUser).hasFieldOrPropertyWithValue("name","New Name");
        assertThat(testUser).hasFieldOrPropertyWithValue("login","New Login");
    }
}
