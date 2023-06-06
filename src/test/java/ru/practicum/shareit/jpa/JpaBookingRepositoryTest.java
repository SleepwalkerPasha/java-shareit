package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.JpaBookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DataJpaTest
class JpaBookingRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JpaBookingRepository jpaBookingRepository;

    private UserDto owner;

    private UserDto booker;

    private BookingDto bookingDto;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        owner = new UserDto();
        owner.setName("name");
        owner.setEmail("email@yandex.ru");

        testEntityManager.persist(owner);

        booker = new UserDto();
        booker.setName("requestor");
        booker.setEmail("emailemail222@yandex.ru");

        testEntityManager.persist(booker);

        itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(owner);

        testEntityManager.persist(itemDto);

        LocalDateTime startDate = LocalDateTime.of(2023, 4, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 30, 12, 0);

        bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);
    }

    @Test
    void testFindBookingDtoByItemIdAndBookerId() {
        LocalDateTime startDate = LocalDateTime.of(2023, 4, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 30, 12, 0);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        List<BookingDto> bookings = jpaBookingRepository.findBookingDtoByItemIdAndBookerId(itemDto.getId(), booker.getId());

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(bookingDto));
    }

    @Test
    void testFindBookingDtoByBookerId() {
        Optional<BookingDto> bookingOpt = jpaBookingRepository.findBookingDtoByBookerId(booker.getId(), bookingDto.getId());

        assertThat(bookingOpt.isPresent(), is(true));
        assertThat(bookingOpt.get(), equalTo(bookingDto));
    }

    @Test
    void testFindBookingDtoByOwnerId() {
        Optional<BookingDto> bookingOpt = jpaBookingRepository.findBookingDtoByOwnerId(owner.getId(), bookingDto.getId());

        assertThat(bookingOpt.isPresent(), is(true));
        assertThat(bookingOpt.get(), equalTo(bookingDto));
    }

    @Test
    void testFindAllPastBookings() {
        Page<BookingDto> allPastBookings = jpaBookingRepository.findAllPastBookings(booker.getId(), PageRequest.of(0, 5));

        assertThat(allPastBookings.getSize(), equalTo(5));
        assertThat(allPastBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllCurrentBookings() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        Page<BookingDto> allCurrentBookings = jpaBookingRepository.findAllCurrentBookings(booker.getId(), PageRequest.of(0, 5));

        assertThat(allCurrentBookings.getSize(), equalTo(5));
        assertThat(allCurrentBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllByBookerId() {
        Page<BookingDto> bookings = jpaBookingRepository.findAllByBookerId(booker.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(5));
        assertThat(bookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllFutureBookings() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(4);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        Page<BookingDto> allFutureBookings = jpaBookingRepository.findAllFutureBookings(booker.getId(), PageRequest.of(0, 5));

        assertThat(allFutureBookings.getSize(), equalTo(5));
        assertThat(allFutureBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllWaitingBookings() {
        Page<BookingDto> allWaitingBookings = jpaBookingRepository.findAllWaitingBookings(booker.getId(), PageRequest.of(0, 5));

        assertThat(allWaitingBookings.getSize(), equalTo(5));
        assertThat(allWaitingBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllRejectedBookings() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(4);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.REJECTED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        Page<BookingDto> allRejectedBookings = jpaBookingRepository.findAllRejectedBookings(booker.getId(), PageRequest.of(0, 5));

        assertThat(allRejectedBookings.getSize(), equalTo(5));
        assertThat(allRejectedBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllPastOwnerBookings() {
        Page<BookingDto> allPastBookings = jpaBookingRepository.findAllPastOwnerBookings(owner.getId(), PageRequest.of(0, 5));

        assertThat(allPastBookings.getSize(), equalTo(5));
        assertThat(allPastBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllCurrentOwnerBookings() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(2);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        Page<BookingDto> allCurrentBookings = jpaBookingRepository.findAllCurrentOwnerBookings(owner.getId(), PageRequest.of(0, 5));

        assertThat(allCurrentBookings.getSize(), equalTo(5));
        assertThat(allCurrentBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllByOwnerId() {
        Page<BookingDto> bookings = jpaBookingRepository.findAllByOwnerId(owner.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(5));
        assertThat(bookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllFutureOwnerBookings() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(4);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        Page<BookingDto> allFutureBookings = jpaBookingRepository.findAllFutureOwnerBookings(owner.getId(), PageRequest.of(0, 5));

        assertThat(allFutureBookings.getSize(), equalTo(5));
        assertThat(allFutureBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllWaitingOwnerBookings() {
        Page<BookingDto> allWaitingBookings = jpaBookingRepository.findAllWaitingOwnerBookings(owner.getId(), PageRequest.of(0, 5));

        assertThat(allWaitingBookings.getSize(), equalTo(5));
        assertThat(allWaitingBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindAllRejectedOwnerBookings() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(4);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.REJECTED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        Page<BookingDto> allRejectedBookings = jpaBookingRepository.findAllRejectedOwnerBookings(owner.getId(), PageRequest.of(0, 5));

        assertThat(allRejectedBookings.getSize(), equalTo(5));
        assertThat(allRejectedBookings.getNumberOfElements(), equalTo(1));
    }

    @Test
    void testFindApprovedBookingByItemId() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(4);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        List<BookingDto> approvedBookingByItemId = jpaBookingRepository.findApprovedBookingByItemId(itemDto.getId());

        assertThat(approvedBookingByItemId.size(), equalTo(1));
        assertThat(approvedBookingByItemId.get(0), equalTo(bookingDto));
    }

    @Test
    void testFindApprovedBookingInItemIds() {
        LocalDateTime startDate = LocalDateTime.now().plusDays(2);
        LocalDateTime endDate = LocalDateTime.now().plusDays(4);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
        bookingDto.setItem(itemDto);

        testEntityManager.persist(bookingDto);

        List<BookingDto> approvedBookingByItemId = jpaBookingRepository.findApprovedBookingInItemIds(List.of(itemDto.getId()));

        assertThat(approvedBookingByItemId.size(), equalTo(1));
        assertThat(approvedBookingByItemId.get(0), equalTo(bookingDto));
    }
}