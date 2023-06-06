package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.JpaUserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testBootstrappingByPersistingAnUser() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        entityManager.persist(userDto);

        assertThat(userDto.getId(), notNullValue());
    }

    @Test
    void testJpaUserRepositorySaveEntity() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        jpaUserRepository.save(userDto);

        assertThat(userDto.getId(), notNullValue());
    }
}