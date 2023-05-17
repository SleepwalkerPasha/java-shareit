package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    Booking addBooking(long userId, Booking booking);

    Booking approveBooking(long userId, long bookingId, boolean approved);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getBookingsByUserId(long userId, BookingState state);

    List<Booking> getAllBookingsOfUser(long ownerId, BookingState state);
}
