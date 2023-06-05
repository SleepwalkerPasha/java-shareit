package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.JpaItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class JpaItemRequestRepositoryTest {

    @Autowired
    private JpaItemRequestRepository jpaItemRequestRepository;


    @Autowired
    private TestEntityManager testEntityManager;

    private UserDto userDto;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");

        testEntityManager.persist(userDto);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);

        testEntityManager.persist(itemRequestDto);
    }

    @Test
    @Rollback
    void testFindAllByRequestor_IdOrderByCreatedDesc() {

        List<ItemRequestDto> itemRequestDtos = jpaItemRequestRepository
                .findAllByRequestor_IdOrderByCreatedDesc(userDto.getId());

        assertThat(itemRequestDtos.size(), equalTo(1));
        assertThat(itemRequestDtos.get(0), equalTo(itemRequestDto));
    }

    @Test
    @Rollback
    void testFindAllRequestorIdNotEqualToRequestor() {
        UserDto userDto = new UserDto();
        userDto.setName("new name");
        userDto.setEmail("email123@yandex.ru");

        testEntityManager.persist(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description 123 aa");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(userDto);

        testEntityManager.persist(itemRequestDto);

        List<ItemRequestDto> requests = jpaItemRequestRepository.findAllItemRequests(userDto.getId());

        assertThat(requests.size(), equalTo(1));
        assertThat(requests.get(0), equalTo(this.itemRequestDto));
    }
}