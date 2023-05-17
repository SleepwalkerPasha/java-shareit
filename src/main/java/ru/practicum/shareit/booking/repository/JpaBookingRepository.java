package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface JpaBookingRepository extends JpaRepository<BookingDto, Long> {
}
