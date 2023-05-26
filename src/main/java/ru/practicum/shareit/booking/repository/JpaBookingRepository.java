package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "where b.booker.id = ?1 and b.endDate <= now() " +
            "order by b.endDate desc")
    Page<BookingDto> findAllPastBookings(long bookerId, Pageable pageable);

    // current
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and now() between b.startDate and b.endDate " +
            "order by b.endDate desc")
    Page<BookingDto> findAllCurrentBookings(long bookerId, Pageable pageable);

    // all
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1" +
            " order by b.endDate desc")
    Page<BookingDto> findAllByBookerId(long bookerId, Pageable pageable);

    // future
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.startDate > now() and b.endDate > now() " +
            "order by b.endDate desc")
    Page<BookingDto> findAllFutureBookings(long bookerId, Pageable pageable);

    // waiting
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.status = 'WAITING' " +
            "order by b.endDate desc")
    Page<BookingDto> findAllWaitingBookings(long bookerId, Pageable pageable);

    // rejected
    @Query("select b from BookingDto b " +
            "where b.booker.id = ?1 and b.status = 'REJECTED' " +
            "order by b.endDate desc")
    Page<BookingDto> findAllRejectedBookings(long bookerId, Pageable pageable);


    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.endDate <= now() " +
            "order by b.endDate desc")
    Page<BookingDto> findAllPastOwnerBookings(long ownerId, Pageable pageable);

    // current
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and now() between b.startDate and b.endDate " +
            "order by b.endDate desc")
    Page<BookingDto> findAllCurrentOwnerBookings(long ownerId, Pageable pageable);

    // all
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1" +
            " order by b.endDate desc")
    Page<BookingDto> findAllByOwnerId(long ownerId, Pageable pageable);

    // future
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.startDate >= now()" +
            "order by b.endDate desc")
    Page<BookingDto> findAllFutureOwnerBookings(long ownerId, Pageable pageable);

    // waiting
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.status = 'WAITING' " +
            "order by b.endDate desc")
    Page<BookingDto> findAllWaitingOwnerBookings(long ownerId, Pageable pageable);

    // rejected
    @Query("select b from BookingDto b " +
            "where b.item.owner.id = ?1 and b.status = 'REJECTED' " +
            "order by b.endDate desc")
    Page<BookingDto> findAllRejectedOwnerBookings(long ownerId, Pageable pageable);


    @Query("select b from BookingDto b " +
            "where b.item.id = ?1 and b.status = 'APPROVED'" +
            " order by b.endDate")
    List<BookingDto> findApprovedBookingByItemId(long itemId);

    @Query("select b from BookingDto b " +
            "where b.item.id in ?1 and b.status = 'APPROVED' " +
            "order by b.endDate")
    List<BookingDto> findApprovedBookingInItemIds(List<Long> itemIds);
}
