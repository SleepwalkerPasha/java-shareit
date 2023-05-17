package ru.practicum.shareit.booking.model;

public enum BookingState {
    CURRENT("CURRENT"),
    PAST("PAST"),
    FUTURE("FUTURE"),
    WAITING("WAITING"),
    REJECTED("REJECTED");


    BookingState(String state) {

    }
}
