package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto approveBooking(BookingDto bookingDto);

    Optional<BookingDto> getBookingByIdOfUserId(long userId, long bookingId);

    List<BookingDto> getBookingsByItemIdAndUserId(long itemId, long userId);

    Optional<BookingDto> getBookingByIdOfOwnerId(long ownerId, long bookingId);

    Page<BookingDto> getPastBookingsByOwnerId(long ownerId, Pageable pageable);

    Page<BookingDto> getCurrentBookingsByOwnerId(long ownerId, Pageable pageable);

    Page<BookingDto> getAllBookingsByOwnerId(long ownerId, Pageable pageable);

    Page<BookingDto> getFutureBookingsByOwnerId(long ownerId, Pageable pageable);

    Page<BookingDto> getWaitingBookingsByOwnerId(long ownerId, Pageable pageable);

    Page<BookingDto> getRejectedBookingsByOwnerId(long ownerId, Pageable pageable);

    Page<BookingDto> getPastBookingsByUserId(long userId, Pageable pageable);

    Page<BookingDto> getCurrentBookingsByUserId(long userId, Pageable pageable);

    Page<BookingDto> getAllBookingsByUserId(long userId, Pageable pageable);

    Page<BookingDto> getFutureBookingsByUserId(long userId, Pageable pageable);

    Page<BookingDto> getWaitingBookingsByUserId(long userId, Pageable pageable);

    Page<BookingDto> getRejectedBookingsByUserId(long userId, Pageable pageable);

    List<BookingDto> getApprovedBookingsByItemId(long itemId);

    List<BookingDto> getApprovedBookingsInItems(List<Long> itemIds);
}
