package ru.practicum.shareit.repos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.JpaItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.JpaUserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
class JpaItemRequestRepositoryTest {

    @Autowired
    private JpaItemRequestRepository jpaItemRequestRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testBootstrappingByPersistingAnItemRequest() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        entityManager.persist(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);

        entityManager.persist(itemRequestDto);

        assertThat(itemRequestDto.getId(), notNullValue());
    }

    @Test
    @Rollback
    void findAllByRequestor_IdOrderByCreatedDesc() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        userRepository.save(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);

        jpaItemRequestRepository.save(itemRequestDto);

        List<ItemRequestDto> itemRequestDtos = jpaItemRequestRepository
                .findAllByRequestor_IdOrderByCreatedDesc(userDto.getId());

        assertThat(itemRequestDtos.size(), equalTo(1));
        assertThat(itemRequestDtos.get(0), equalTo(itemRequestDto));
    }

    @Test
    @Rollback
    void testFindPageAllItemRequests() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        userRepository.save(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);

        jpaItemRequestRepository.save(itemRequestDto);

        Page<ItemRequestDto> itemRequestDtos = jpaItemRequestRepository.findAllItemRequests(userDto.getId(), PageRequest.of(0, 5));

        assertThat(itemRequestDtos.getTotalElements(), equalTo(1L));
    }

    @Test
    @Rollback
    void testFindAllItemRequests() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        userRepository.save(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);

        jpaItemRequestRepository.save(itemRequestDto);

        List<ItemRequestDto> itemRequestDtos = jpaItemRequestRepository.findAllItemRequests(userDto.getId());

        assertThat(itemRequestDtos.size(), equalTo(1));
    }
}