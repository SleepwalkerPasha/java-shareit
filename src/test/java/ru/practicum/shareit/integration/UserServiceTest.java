package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;

    private User owner;

    private User booker;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("user");
        user.setEmail("email@mail.ru");

        owner = userService.addUser(user);

        User newUser = new User();
        newUser.setName("user2");
        newUser.setEmail("someemail@mail.ru");

        booker = userService.addUser(newUser);
    }

    @Test
    @Rollback
    @Transactional
    void testGetUserById() {
        Optional<User> userById = userService.getUserById(owner.getId());

        assertThat(userById.isPresent(), is(true));
        assertThat(userById.get(), equalTo(owner));
    }


    @Test
    @Rollback
    @Transactional
    void testGetAll() {
        List<User> users = userService.getAll();

        assertThat(users.size(), equalTo(2));
        assertThat(users.get(0), equalTo(owner));
        assertThat(users.get(1), equalTo(booker));
    }
}
