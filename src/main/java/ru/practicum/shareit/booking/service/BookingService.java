package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    Booking addBooking(long userId, BookingRequest booking);

    Booking approveBooking(long ownerId, long bookingId, boolean approved);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getBookingsByUserId(long userId, BookingState state, int from, int size);

    List<Booking> getBookingsByOwnerId(long ownerId, BookingState state, int from, int size);
}
