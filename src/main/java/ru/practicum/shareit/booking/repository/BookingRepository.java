package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {

    BookingDto addBooking(long userId, BookingDto booking);

    BookingDto approveBooking(long userId, long bookingId, boolean approved);

    Optional<BookingDto> getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsByUserId(long userId, BookingState state);

    List<BookingDto> getAllBookingsOfUser(long ownerId, BookingState state);
}
