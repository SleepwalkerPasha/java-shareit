package ru.practicum.shareit.unit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepositoryImpl;
import ru.practicum.shareit.booking.repository.JpaBookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingRepositoryImplTest {

    @Mock
    JpaBookingRepository jpaBookingRepository;

    @InjectMocks
    BookingRepositoryImpl bookingRepository;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UserDto ownerDto;
    private UserDto userDto;
    private ItemDto itemDto;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        bookingRequest.setStart(startDate);
        bookingRequest.setEnd(endDate);

        ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        userDto = new UserDto(2L, "user", "adadad133@ya.ru");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(userDto);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);
    }

    @Test
    void testGetBookingByIdOfUserId() {
        when(jpaBookingRepository.findBookingDtoByBookerId(anyLong(), anyLong())).thenReturn(Optional.of(bookingDto));

        Optional<BookingDto> bookingByIdOfUserId = bookingRepository.getBookingByIdOfUserId(userDto.getId(), bookingDto.getId());

        assertThat(bookingByIdOfUserId.isPresent(), is(true));
        assertThat(bookingByIdOfUserId.get(), equalTo(bookingDto));
    }

    @Test
    void testGetPastBookingsByOwnerIdPageable() {
        startDate = LocalDateTime.now().minusDays(3);
        endDate = LocalDateTime.now().minusDays(1);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(jpaBookingRepository.findAllPastOwnerBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> pastBookingsByOwnerId = bookingRepository.getPastBookingsByOwnerId(ownerDto.getId(), PageRequest.of(0, 5));

        assertThat(pastBookingsByOwnerId.getSize(), equalTo(1));
    }


    @Test
    void testGetCurrentBookingsByOwnerIdPageable() {
        startDate = LocalDateTime.now().minusDays(1);
        endDate = LocalDateTime.now().plusDays(3);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(jpaBookingRepository.findAllCurrentOwnerBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getCurrentBookingsByOwnerId(ownerDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }

    @Test
    void testGetFutureBookingsByOwnerIdPageable() {
        startDate = LocalDateTime.now().plusDays(1);
        endDate = LocalDateTime.now().plusDays(3);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(jpaBookingRepository.findAllFutureOwnerBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getFutureBookingsByOwnerId(ownerDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetWaitingBookingsByOwnerIdPageable() {
        when(jpaBookingRepository.findAllWaitingOwnerBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getWaitingBookingsByOwnerId(ownerDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetRejectedBookingsByOwnerIdPageable() {
        bookingDto.setStatus(BookingStatus.REJECTED);

        when(jpaBookingRepository.findAllRejectedOwnerBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getRejectedBookingsByOwnerId(ownerDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetPastBookingsByUserIdPageable() {
        startDate = LocalDateTime.now().minusDays(2);
        endDate = LocalDateTime.now().minusDays(1);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(jpaBookingRepository.findAllPastBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getPastBookingsByUserId(userDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }

    @Test
    void testGetCurrentBookingsByUserIdPageable() {
        startDate = LocalDateTime.now().minusDays(2);
        endDate = LocalDateTime.now().plusDays(4);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(jpaBookingRepository.findAllCurrentBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getCurrentBookingsByUserId(userDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetFutureBookingsByUserIdPageable() {
        startDate = LocalDateTime.now().plusDays(2);
        endDate = LocalDateTime.now().plusDays(4);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(jpaBookingRepository.findAllFutureBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getFutureBookingsByUserId(userDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetWaitingBookingsByUserIdPageable() {
        when(jpaBookingRepository.findAllWaitingBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getWaitingBookingsByUserId(userDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetRejectedBookingsByUserIdPageable() {
        bookingDto.setStatus(BookingStatus.REJECTED);
        when(jpaBookingRepository.findAllRejectedBookings(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getRejectedBookingsByUserId(userDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }


    @Test
    void testGetApprovedBookingsByItemId() {
        bookingDto.setStatus(BookingStatus.APPROVED);
        when(jpaBookingRepository.findApprovedBookingByItemId(anyLong())).thenReturn(List.of(bookingDto));

        List<BookingDto> bookings = bookingRepository.getApprovedBookingsByItemId(itemDto.getId());

        assertThat(bookings.size(), equalTo(1));
        assertThat(bookings.get(0), equalTo(bookingDto));
    }


    @Test
    void testGetAllBookingsByUserIdPageable() {
        when(jpaBookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        Page<BookingDto> bookings = bookingRepository.getAllBookingsByUserId(userDto.getId(), PageRequest.of(0, 5));

        assertThat(bookings.getSize(), equalTo(1));
    }
}