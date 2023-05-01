package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING("WAITING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELED("CANCELED");

    BookingStatus(String approved) {

    }
}
