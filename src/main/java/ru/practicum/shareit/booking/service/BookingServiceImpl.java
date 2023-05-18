package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    @Override
    public Booking addBooking(long userId, Booking booking) {
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(UserMapper.toUser(checkForUser(userId)));
        return BookingMapper.toBooking(bookingRepository.addBooking(userId, BookingMapper.toBookingDto(booking)));
    }

    @Override
    public Booking approveBooking(long ownerId, long bookingId, boolean approved) {
        BookingDto bookingDto = checkForOwner(ownerId, bookingId);
        if (approved) {
            bookingDto.setStatus(BookingStatus.APPROVED);
        } else {
            bookingDto.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBooking(bookingRepository.approveBooking(bookingDto));
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        checkForUser(userId);
        Optional<BookingDto> bookingDto = bookingRepository.getBookingByIdOfOwnerId(userId, bookingId);
        if (bookingDto.isEmpty())
            bookingDto = bookingRepository.getBookingByIdOfUserId(userId, bookingId);
        if (bookingDto.isEmpty())
            throw new NotFoundException("данный пользователь не брал вещь в аренду");
        return BookingMapper.toBooking(bookingDto.get());
    }

    @Override
    public List<Booking> getBookingsByUserId(long userId, BookingState state) {
        List<BookingDto> returnedList;
        switch (state) {
            case PAST:
                returnedList = bookingRepository.getPastBookingsByUserId(userId);
                break;
            case FUTURE:
                returnedList = bookingRepository.getFutureBookingsByUserId(userId);
                break;
            case CURRENT:
                returnedList = bookingRepository.getCurrentBookingsByUserId(userId);
                break;
            case WAITING:
                returnedList = bookingRepository.getWaitingBookingsByUserId(userId);
                break;
            case REJECTED:
                returnedList = bookingRepository.getRejectedBookingsByUserId(userId);
                break;
            default:
                returnedList = null;
                break;
        }
        if (returnedList == null)
            throw new IllegalArgumentException("нет такого параметра BookingState");
        return returnedList.stream()
                .map(BookingMapper::toBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByOwnerId(long ownerId, BookingState state) {
        List<BookingDto> returnedList;
        switch (state) {
            case PAST:
                returnedList = bookingRepository.getPastBookingsByOwnerId(ownerId);
                break;
            case FUTURE:
                returnedList = bookingRepository.getFutureBookingsByOwnerId(ownerId);
                break;
            case CURRENT:
                returnedList = bookingRepository.getCurrentBookingsByOwnerId(ownerId);
                break;
            case WAITING:
                returnedList = bookingRepository.getWaitingBookingsByOwnerId(ownerId);
                break;
            case REJECTED:
                returnedList = bookingRepository.getRejectedBookingsByOwnerId(ownerId);
                break;
            default:
                returnedList = null;
                break;
        }
        if (returnedList == null)
            throw new IllegalArgumentException("нет такого параметра BookingState");
        return returnedList.stream()
                .map(BookingMapper::toBooking)
                .collect(Collectors.toList());
    }


    private UserDto checkForUser(long userId) {
        Optional<UserDto> userById = userRepository.getUserById(userId);
        if (userById.isEmpty())
            throw new NotFoundException("такого пользователя нет");
        else
            return userById.get();
    }

    private BookingDto checkForOwner(long ownerId, long bookingId) {
        Optional<BookingDto> bookingDto = bookingRepository.getBookingByIdOfOwnerId(ownerId, bookingId);
        if (bookingDto.isEmpty())
            throw new NotFoundException("нет такого запроса на шеринг у этого владельца");
        else
            return bookingDto.get();
    }
}
