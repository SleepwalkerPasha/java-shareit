package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface JpaBookingRepository extends JpaRepository<BookingDto, Long> {

    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.id = ?2")
    Optional<BookingDto> findBookingDtoByBookerId(long userId, long bookingId);


    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.id = ?2")
    Optional<BookingDto> findBookingDtoByOwnerId(long ownerId, long bookingId);

    // past
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.end_date <= now() " +
            "order by b.start_date")
    List<BookingDto> findAllPastBookings(long bookerId);

    // current
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and now() between b.start_date and b.end_date " +
            "order by b.start_date")
    List<BookingDto> findAllCurrentBookings(long bookerId);

    // all
    List<BookingDto> findAllByBooker_IdOrderByStart_date(long bookerId);

    // future
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.start_date >= now()" +
            "order by b.start_date")
    List<BookingDto> findAllFutureBookings(long bookerId);

    // waiting
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.status = 'WAITING' " +
            "order by b.start_date")
    List<BookingDto> findAllWaitingBookings(long bookerId);

    // rejected
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.status = 'REJECTED' " +
            "order by b.start_date")
    List<BookingDto> findAllRejectedBookings(long bookerId);


    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.end_date <= now() " +
            "order by b.start_date")
    List<BookingDto> findAllPastOwnerBookings(long ownerId);

    // current
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and now() between b.start_date and b.end_date " +
            "order by b.start_date")
    List<BookingDto> findAllCurrentOwnerBookings(long ownerId);

    // all
    List<BookingDto> findAllByItemOwner_IdOrderByStart_date(long ownerId);

    // future
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.start_date >= now()" +
            "order by b.start_date")
    List<BookingDto> findAllFutureOwnerBookings(long ownerId);

    // waiting
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.status = 'WAITING' " +
            "order by b.start_date")
    List<BookingDto> findAllWaitingOwnerBookings(long ownerId);

    // rejected
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.status = 'REJECTED' " +
            "order by b.start_date")
    List<BookingDto> findAllRejectedOwnerBookings(long ownerId);
}
