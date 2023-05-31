package ru.practicum.shareit.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepositoryImpl;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class BookingServiceImplTest {

    @Mock
    private BookingRepositoryImpl bookingRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private ItemRepositoryImpl itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        LocalDateTime startDate = LocalDateTime.of(2023, 4, 30, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 5, 30, 12, 0);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(1L);
        bookingRequest.setStart(startDate);
        bookingRequest.setEnd(endDate);

        UserDto ownerDto = new UserDto(1L, "owner", "email@mail.ru");

        UserDto userDto = new UserDto(2L, "user", "adadad133@ya.ru");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setOwner(ownerDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(userDto);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        BookingDto bookingDtoApproved = new BookingDto();
        bookingDtoApproved.setId(1L);
        bookingDtoApproved.setStatus(BookingStatus.APPROVED);
        bookingDtoApproved.setItem(itemDto);
        bookingDtoApproved.setBooker(userDto);
        bookingDtoApproved.setStartDate(startDate);
        bookingDtoApproved.setEndDate(endDate);

        when(userRepository.getUserById(1L)).thenReturn(Optional.of(ownerDto));

        when(userRepository.getUserById(2L)).thenReturn(Optional.of(userDto));

        when(itemRepository.getItem(1L)).thenReturn(Optional.of(itemDto));

        when(bookingRepository.addBooking(anyLong(), any())).thenReturn(bookingDto);

        when(bookingRepository.getBookingByIdOfOwnerId(1L, 1L)).thenReturn(Optional.of(bookingDto));

        when(bookingRepository.getWaitingBookingsByUserId(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        when(bookingRepository.getWaitingBookingsByOwnerId(anyLong(), any())).thenReturn(new PageImpl<>(List.of(bookingDto)));

        when(bookingRepository.approveBooking(any())).thenReturn(bookingDtoApproved);

        when(bookingRepository.getBookingByIdOfUserId(2L, 1L)).thenReturn(Optional.of(bookingDto));

        bookingService.addBooking(2L, bookingRequest);
    }

    @Test
    void testAddBooking() {
        LocalDateTime startDate = LocalDateTime.of(2023, 5, 20, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 6, 20, 12, 0);

        UserDto owner = userRepository.getUserById(2L).get();
        UserDto booker = userRepository.getUserById(1L).get();

        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setName("item 2");
        itemDto.setDescription("description 2");
        itemDto.setAvailable(true);
        itemDto.setOwner(owner);

        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setItemId(2L);
        bookingRequest.setStart(startDate);
        bookingRequest.setEnd(endDate);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(2L);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(itemDto);
        bookingDto.setBooker(booker);
        bookingDto.setStartDate(startDate);
        bookingDto.setEndDate(endDate);

        when(bookingRepository.addBooking(anyLong(), any())).thenReturn(bookingDto);

        when(itemRepository.getItem(2L)).thenReturn(Optional.of(itemDto));

        Booking booking = bookingService.addBooking(1L, bookingRequest);

        assertThat(booking.getId(), equalTo(2L));
        assertThat(booking.getStatus(), equalTo(bookingDto.getStatus()));
        assertThat(booking.getItem(), equalTo(ItemMapper.toItem(bookingDto.getItem())));

        verify(bookingRepository, times(2)).addBooking(anyLong(), any());
        verify(itemRepository, times(1)).getItem(2L);
        verify(userRepository, times(2)).getUserById(1L);
    }

    @Test
    void testApproveBooking() {
        Booking booking = bookingService.approveBooking(1L, 1L, true);
        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getStatus(), equalTo(BookingStatus.APPROVED));

        verify(bookingRepository, times(1)).approveBooking(any());
        verify(bookingRepository, times(1)).getBookingByIdOfOwnerId(1L, 1L);
    }

    @Test
    void testGetBookingById() {
        Booking bookingById = bookingService.getBookingById(1L, 1L);

        assertThat(bookingById.getId(), equalTo(1L));
        assertThat(bookingById.getBooker().getId(), equalTo(2L));
        assertThat(bookingById.getItem().getId(), equalTo(1L));

        verify(bookingRepository, times(1)).getBookingByIdOfOwnerId(1L, 1L);
        verify(bookingRepository, times(0)).getBookingByIdOfUserId(2L, 1L);
    }

    @Test
    void testGetBookingsByUserId() {
        List<Booking> bookingsByUserId = bookingService.getBookingsByUserId(2L, BookingState.WAITING, 0, 5);

        assertThat(bookingsByUserId.size(), equalTo(1));
        assertThat(bookingsByUserId.get(0).getId(), equalTo(1L));
        assertThat(bookingsByUserId.get(0).getBooker().getId(), equalTo(2L));
        assertThat(bookingsByUserId.get(0).getStatus(), equalTo(BookingStatus.WAITING));

        verify(userRepository, times(2)).getUserById(2L);
        verify(bookingRepository, times(1)).getWaitingBookingsByUserId(anyLong(), any());
        verify(bookingRepository, times(0)).getWaitingBookingsByUserId(anyLong());
    }

    @Test
    void testGetBookingsByOwnerId() {
        List<Booking> bookingsByOwnerId = bookingService.getBookingsByOwnerId(1L, BookingState.WAITING, 0, 5);

        assertThat(bookingsByOwnerId.size(), equalTo(1));
        assertThat(bookingsByOwnerId.get(0).getId(), equalTo(1L));
        assertThat(bookingsByOwnerId.get(0).getItem().getOwner().getId(), equalTo(1L));
        assertThat(bookingsByOwnerId.get(0).getStatus(), equalTo(BookingStatus.WAITING));

        verify(userRepository, times(1)).getUserById(1L);
        verify(bookingRepository, times(1)).getWaitingBookingsByOwnerId(anyLong(), any());
        verify(bookingRepository, times(0)).getWaitingBookingsByOwnerId(anyLong());
    }
}