package ru.practicum.shareit.booking.model;

public enum BookingState {
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    WAITING("WAITING"),
    ALL("ALL"),
    UNSUPPORTED_STATUS("UNSUPPORTED_STATUS"),
    REJECTED("REJECTED");


    BookingState(String state) {

    }
}
