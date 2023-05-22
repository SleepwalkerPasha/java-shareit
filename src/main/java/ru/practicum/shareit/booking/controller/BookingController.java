package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingRequest;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotValidBookingRequestException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking addBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId, @RequestBody @Validated BookingRequest booking) {
        validateBookingRequest(booking);
        return bookingService.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking approveBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam(name = "approved") boolean available) {
        return bookingService.approveBooking(userId, bookingId, available);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@RequestHeader(name = "X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingsByUserId(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                             @RequestParam(name = "state",
                                                     required = false,
                                                     defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsOfUser(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state",
                                                      required = false,
                                              defaultValue = "ALL") BookingState state) {
        return bookingService.getBookingsByOwnerId(userId, state);
    }

    private void validateBookingRequest(BookingRequest bookingRequest) {
        if (bookingRequest.getEnd().isBefore(bookingRequest.getStart()))
            throw new NotValidBookingRequestException("конец раньше старта бронирования");
        else if (bookingRequest.getStart().equals(bookingRequest.getEnd()))
            throw new NotValidBookingRequestException("конец равен началу бронирования");
        else if (bookingRequest.getStart().isBefore(LocalDateTime.now()))
            throw new NotValidBookingRequestException("старт бронирования в прошлом");
    }
}
