package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.sql.Timestamp;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        if (bookingDto.getId() != null)
            booking.setId(booking.getId());
        if (bookingDto.getStatus() != null)
            booking.setStatus(bookingDto.getStatus());
        if (bookingDto.getBooker() != null)
            booking.setBooker(UserMapper.toUser(bookingDto.getBooker()));
        if (bookingDto.getStart_date() != null)
            booking.setStart(bookingDto.getStart_date().toInstant());
        if (bookingDto.getEnd_date() != null)
            booking.setEnd(bookingDto.getEnd_date().toInstant());
        if (bookingDto.getItem() != null)
            booking.setItem(ItemMapper.toItem(bookingDto.getItem()));
        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        if (booking.getId() != null)
            bookingDto.setId(booking.getId());
        if (booking.getStatus() != null)
            bookingDto.setStatus(booking.getStatus());
        if (booking.getBooker() != null)
            bookingDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        if (booking.getStart() != null)
            bookingDto.setStart_date(Timestamp.from(booking.getStart()));
        if (booking.getEnd() != null)
            bookingDto.setEnd_date(Timestamp.from(booking.getEnd()));
        if (booking.getItem() != null)
            bookingDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        return bookingDto;
    }
}
