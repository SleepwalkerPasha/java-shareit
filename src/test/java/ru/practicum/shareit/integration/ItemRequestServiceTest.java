package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    private final ItemRequestService requestService;

    private final UserService userService;

    private final ItemService itemService;

    private User booker;

    private User owner;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("user");
        user.setEmail("email@mail.ru");

        owner = userService.addUser(user);

        Item item = new Item();
        item.setName("item");
        item.setDescription("description");
        item.setOwner(owner);
        item.setAvailable(true);

        itemService.addItem(item, owner.getId());
        ItemRequest request = new ItemRequest();
        request.setDescription("need item 1");

        User newUser = new User();
        newUser.setName("user2");
        newUser.setEmail("someemail@mail.ru");

        booker = userService.addUser(newUser);

        itemRequest = requestService.addRequest(booker.getId(), request);
    }

    @Test
    @Rollback
    @Transactional
    void testGetAllRequests() {
        List<ItemRequest> allRequests = requestService.getAllRequests(owner.getId(), null, null);

        assertThat(allRequests.size(), equalTo(1));
        assertThat(allRequests.get(0).getId(), equalTo(itemRequest.getId()));
        assertThat(allRequests.get(0).getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    @Rollback
    @Transactional
    void testGetRequestById() {
        ItemRequest requestById = requestService.getRequestById(booker.getId(), itemRequest.getId());

        assertThat(requestById.getId(), equalTo(itemRequest.getId()));
        assertThat(requestById.getDescription(), equalTo(itemRequest.getDescription()));
    }

}
