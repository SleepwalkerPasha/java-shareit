package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Override
    public Booking addBooking(long userId, Booking booking) {
        return null;
    }

    @Override
    public Booking approveBooking(long userId, long bookingId, boolean approved) {
        return null;
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        return null;
    }

    @Override
    public List<Booking> getBookingsByUserId(long userId, BookingState state) {
        return null;
    }

    @Override
    public List<Booking> getAllBookingsOfUser(long ownerId, BookingState state) {
        return null;
    }
}
