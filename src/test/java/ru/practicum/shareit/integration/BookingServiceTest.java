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
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
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
public class BookingServiceTest {

    private final BookingService bookingService;

    private final UserService userService;

    private final ItemService itemService;

    private User booker;

    private User owner;

    private Booking booking;

    @BeforeEach
    void setUp() {
        LocalDateTime startDate = LocalDateTime.of(2023, 4, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 30, 12, 0);

        User user = new User();
        user.setName("user");
        user.setEmail("email@mail.ru");

        owner = userService.addUser(user);

        Item item = new Item();
        item.setName("item");
        item.setDescription("description");
        item.setOwner(owner);
        item.setAvailable(true);

        Item addedItem = itemService.addItem(item, owner.getId());
        ItemRequest request = new ItemRequest();
        request.setDescription("need item 1");

        User newUser = new User();
        newUser.setName("user2");
        newUser.setEmail("someemail@mail.ru");

        booker = userService.addUser(newUser);

        BookingRequest bookingRequest = new BookingRequest(addedItem.getId(), startDate, endDate);

        booking = bookingService.addBooking(booker.getId(), bookingRequest);
    }

    @Test
    @Rollback
    @Transactional
     void testGetBookingsByUserId() {
        List<Booking> bookingsByUserId = bookingService.getBookingsByUserId(booker.getId(), BookingState.ALL, 0, 5);

        assertThat(bookingsByUserId.size(), equalTo(1));
        assertThat(bookingsByUserId.get(0).getId(), equalTo(booking.getId()));
        assertThat(bookingsByUserId.get(0).getBooker().getId(), equalTo(booker.getId()));
    }

    @Test
    @Rollback
    @Transactional
    void testGetBookingsByOwnerId() {
        List<Booking> bookingsByOwnerId = bookingService.getBookingsByOwnerId(owner.getId(), BookingState.ALL, 0, 5);

        assertThat(bookingsByOwnerId.size(), equalTo(1));
        assertThat(bookingsByOwnerId.get(0).getId(), equalTo(booking.getId()));
        assertThat(bookingsByOwnerId.get(0).getItem().getOwner().getId(), equalTo(owner.getId()));
    }
}
