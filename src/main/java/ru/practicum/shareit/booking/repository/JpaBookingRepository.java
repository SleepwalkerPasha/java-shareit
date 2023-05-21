package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;
import java.util.Optional;

public interface JpaBookingRepository extends JpaRepository<BookingDto, Long> {


    @Query("select b from BookingDto b " +
            "where b.item.id = ?1 and b.booker.id = ?2 and b.status = 'APPROVED'")
    List<BookingDto> findBookingDtoByItemIdAndBookerId(long itemId, long userId);

    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.id = ?2")
    Optional<BookingDto> findBookingDtoByBookerId(long userId, long bookingId);


    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.id = ?2")
    Optional<BookingDto> findBookingDtoByOwnerId(long ownerId, long bookingId);

    // past
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.end_date <= now() " +
            "order by b.end_date desc")
    List<BookingDto> findAllPastBookings(long bookerId);

    // current
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and now() between b.start_date and b.end_date " +
            "order by b.end_date desc")
    List<BookingDto> findAllCurrentBookings(long bookerId);

    // all
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1" +
            " order by b.end_date desc")
    List<BookingDto> findAllByBookerId(long bookerId);

    // future
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.start_date > now() and b.end_date > now() " +
            "order by b.end_date desc")
    List<BookingDto> findAllFutureBookings(long bookerId);

    // waiting
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.status = 'WAITING' " +
            "order by b.end_date desc")
    List<BookingDto> findAllWaitingBookings(long bookerId);

    // rejected
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.status = 'REJECTED' " +
            "order by b.end_date desc")
    List<BookingDto> findAllRejectedBookings(long bookerId);


    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.end_date <= now() " +
            "order by b.end_date desc")
    List<BookingDto> findAllPastOwnerBookings(long ownerId);

    // current
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and now() between b.start_date and b.end_date " +
            "order by b.end_date desc")
    List<BookingDto> findAllCurrentOwnerBookings(long ownerId);

    // all
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1" +
            " order by b.end_date desc")
    List<BookingDto> findAllByOwnerId(long ownerId);

    // future
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.start_date >= now()" +
            "order by b.end_date desc")
    List<BookingDto> findAllFutureOwnerBookings(long ownerId);

    // waiting
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.status = 'WAITING' " +
            "order by b.end_date desc")
    List<BookingDto> findAllWaitingOwnerBookings(long ownerId);

    // rejected
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.status = 'REJECTED' " +
            "order by b.end_date desc")
    List<BookingDto> findAllRejectedOwnerBookings(long ownerId);


    @Query("select b from BookingDto b " +
            "where b.item.id = ?1 " +
            "order by b.end_date")
    List<BookingDto> findBookingByItemId(long itemId);

}
