package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final JpaBookingRepository jpaBookingRepository;

    @Override
    public BookingDto addBooking(long userId, BookingDto booking) {
        return jpaBookingRepository.save(booking);
    }

    @Override
    public BookingDto approveBooking(BookingDto bookingDto) {
        return jpaBookingRepository.save(bookingDto);
    }

    @Override
    public Optional<BookingDto> getBookingByIdOfUserId(long userId, long bookingId) {
        return jpaBookingRepository.findBookingDtoByBookerId(userId, bookingId);
    }

    @Override
    public Optional<BookingDto> getBookingByItemIdAndUserId(long userId, long itemId) {
        return jpaBookingRepository.findBookingByUserIdAndItemId(userId, itemId);
    }

    @Override
    public Optional<BookingDto> getBookingByIdOfOwnerId(long ownerId, long bookingId) {
        return jpaBookingRepository.findBookingDtoByOwnerId(ownerId, bookingId);
    }


    @Override
    public List<BookingDto> getPastBookingsByOwnerId(long ownerId) {
        return jpaBookingRepository.findAllPastOwnerBookings(ownerId);
    }

    @Override
    public List<BookingDto> getCurrentBookingsByOwnerId(long ownerId) {
        return jpaBookingRepository.findAllCurrentOwnerBookings(ownerId);
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(long ownerId) {
        return jpaBookingRepository.findAllByOwnerId(ownerId);
    }

    @Override
    public List<BookingDto> getFutureBookingsByOwnerId(long ownerId) {
        return jpaBookingRepository.findAllFutureOwnerBookings(ownerId);
    }

    @Override
    public List<BookingDto> getWaitingBookingsByOwnerId(long ownerId) {
        return jpaBookingRepository.findAllWaitingOwnerBookings(ownerId);
    }

    @Override
    public List<BookingDto> getRejectedBookingsByOwnerId(long ownerId) {
        return jpaBookingRepository.findAllRejectedOwnerBookings(ownerId);
    }

    @Override
    public List<BookingDto> getPastBookingsByUserId(long userId) {
        return jpaBookingRepository.findAllPastBookings(userId);
    }

    @Override
    public List<BookingDto> getCurrentBookingsByUserId(long userId) {
        return jpaBookingRepository.findAllCurrentBookings(userId);
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(long userId) {
        return jpaBookingRepository.findAllByBookerId(userId);
    }

    @Override
    public List<BookingDto> getFutureBookingsByUserId(long userId) {
        return jpaBookingRepository.findAllFutureBookings(userId);
    }

    @Override
    public List<BookingDto> getWaitingBookingsByUserId(long userId) {
        return jpaBookingRepository.findAllWaitingBookings(userId);
    }

    @Override
    public List<BookingDto> getRejectedBookingsByUserId(long userId) {
        return jpaBookingRepository.findAllRejectedBookings(userId);
    }

    @Override
    public Optional<BookingDto> getLastBookingByItemId(long itemId) {
        return jpaBookingRepository.findLastBooking(itemId);
    }

}
