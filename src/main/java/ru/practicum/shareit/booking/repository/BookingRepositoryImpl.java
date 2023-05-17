package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final JpaBookingRepository jpaBookingRepository;

    @Override
    public BookingDto addBooking(long userId, BookingDto booking) {
        return null;
    }

    @Override
    public BookingDto approveBooking(long userId, long bookingId, boolean approved) {
        return null;
    }

    @Override
    public Optional<BookingDto> getBookingById(long userId, long bookingId) {
        return Optional.empty();
    }

    @Override
    public List<BookingDto> getBookingsByUserId(long userId, BookingState state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllBookingsOfUser(long ownerId, BookingState state) {
        return null;
    }
}
