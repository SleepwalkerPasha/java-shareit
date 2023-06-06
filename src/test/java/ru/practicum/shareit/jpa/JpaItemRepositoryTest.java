package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.JpaItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class JpaItemRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JpaItemRepository jpaItemRepository;

    private UserDto owner;

    private UserDto requestor;

    private ItemRequestDto itemRequestDto;

    private ItemDto itemDto;


    @BeforeEach
    void setUp() {
        owner = new UserDto();
        owner.setName("name");
        owner.setEmail("email@yandex.ru");

        testEntityManager.persist(owner);

        requestor = new UserDto();
        requestor.setName("requestor");
        requestor.setEmail("emailemail222@yandex.ru");

        testEntityManager.persist(requestor);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(requestor);

        testEntityManager.persist(itemRequestDto);

        itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(owner);
        itemDto.setItemRequest(itemRequestDto);

        testEntityManager.persist(itemDto);
    }

    @Test
    void testFindAllItemsBySubstring() {
        List<ItemDto> items = jpaItemRepository.findAllItemsBySubstring("desc");

        assertThat(items.size(), equalTo(1));
    }

    @Test
    void testFindAllUserItemsByUserId() {
        List<ItemDto> items = jpaItemRepository.findAllUserItemsByUserId(owner.getId());

        assertThat(items.size(), equalTo(1));
    }

    @Test
    void testFindAllInItemsRequests() {
        List<Long> requestIds = List.of(itemRequestDto.getId());

        List<ItemDto> allInItemRequests = jpaItemRepository.findAllInItemRequests(requestIds);

        assertThat(allInItemRequests.size(), equalTo(1));
        assertThat(allInItemRequests.get(0), equalTo(itemDto));
    }
}