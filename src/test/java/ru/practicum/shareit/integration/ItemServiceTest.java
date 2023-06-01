package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingInfo;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ShareItApp.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final UserService userService;

    private final ItemService itemService;

    private final BookingService bookingService;

    private User owner;

    private User booker;

    private Item addedItem;

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

        addedItem = itemService.addItem(item, owner.getId());
        ItemRequest request = new ItemRequest();
        request.setDescription("need item 1");

        User newUser = new User();
        newUser.setName("user2");
        newUser.setEmail("someemail@mail.ru");

        booker = userService.addUser(newUser);
    }

    @Test
    @Rollback
    @Transactional
    void testGetAllUserItems() {
        List<ItemBookingInfo> allUserItems = itemService.getAllUserItems(owner.getId(), 0, 5);

        assertThat(allUserItems.size(), equalTo(1));
        assertThat(allUserItems.get(0).getId(), equalTo(addedItem.getId()));
        assertThat(allUserItems.get(0).getName(), equalTo(addedItem.getName()));
    }

    @Test
    @Rollback
    @Transactional
    void testGetItemsByDescription() {
        List<Item> itemByDescription = itemService.getItemByDescription("desc", booker.getId(), 0, 5);

        assertThat(itemByDescription.size(), equalTo(1));
        assertThat(itemByDescription.get(0).getId(), equalTo(addedItem.getId()));
        assertThat(itemByDescription.get(0).getName(), equalTo(addedItem.getName()));
    }

    @Test
    @Rollback
    @Transactional
    void testAddCommentAndGetItemByIdWithComments() {
        LocalDateTime startDate = LocalDateTime.of(2023, 4, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 30, 12, 0);

        BookingRequest bookingRequest = new BookingRequest(addedItem.getId(), startDate, endDate);

        Booking booking = bookingService.addBooking(booker.getId(), bookingRequest);

        bookingService.approveBooking(owner.getId(), booking.getId(), true);

        Comment comment = new Comment();
        comment.setText("comment");
        Comment addedCommentToItem = itemService.addCommentToItem(addedItem.getId(), booker.getId(), comment);

        ItemBookingInfo item = itemService.getItem(addedItem.getId(), booker.getId());

        assertThat(item.getId(), equalTo(addedItem.getId()));
        assertThat(item.getComments().size(), equalTo(1));
        assertThat(item.getComments().get(0).getId(), equalTo(addedCommentToItem.getId()));
        assertThat(item.getComments().get(0).getText(), equalTo(addedCommentToItem.getText()));
        assertThat(item.getComments().get(0).getAuthorName(), equalTo(booker.getName()));
    }

}
