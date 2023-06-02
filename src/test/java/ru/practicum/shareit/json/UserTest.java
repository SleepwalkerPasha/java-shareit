package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@JsonTest
public class UserTest {

    @Autowired
    private JacksonTester<User> json;

    @Test
    void testSerialize() throws IOException {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail.ru");

        String expectedJson = "{\"id\":null,\"name\":\"name\",\"email\":\"email@mail.ru\"}";

        assertThat(json.write(user).getJson(), equalTo(expectedJson));
    }

    @Test
    void testDeserialize() throws IOException {
        String context = "{\"name\":\"name\",\"email\":\"email@mail.ru\"}";

        User expectedUser = new User(null, "name", "email@mail.ru");

        assertThat(json.parseObject(context), equalTo(expectedUser));
    }
}
