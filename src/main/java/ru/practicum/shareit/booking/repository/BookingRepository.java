package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto approveBooking(BookingDto bookingDto);

    Optional<BookingDto> getBookingByIdOfUserId(long userId, long bookingId);

    Optional<BookingDto> getBookingByItemIdAndUserId(long userId, long itemId);

    Optional<BookingDto> getBookingByIdOfOwnerId(long ownerId, long bookingId);

    List<BookingDto> getPastBookingsByOwnerId(long ownerId);

    List<BookingDto> getCurrentBookingsByOwnerId(long ownerId);

    List<BookingDto> getAllBookingsByOwnerId(long ownerId);

    List<BookingDto> getFutureBookingsByOwnerId(long ownerId);

    List<BookingDto> getWaitingBookingsByOwnerId(long ownerId);

    List<BookingDto> getRejectedBookingsByOwnerId(long ownerId);

    List<BookingDto> getPastBookingsByUserId(long userId);

    List<BookingDto> getCurrentBookingsByUserId(long userId);

    List<BookingDto> getAllBookingsByUserId(long userId);

    List<BookingDto> getFutureBookingsByUserId(long userId);

    List<BookingDto> getWaitingBookingsByUserId(long userId);

    List<BookingDto> getRejectedBookingsByUserId(long userId);

    Optional<BookingDto> getLastBookingByItemId(long itemId);
}
