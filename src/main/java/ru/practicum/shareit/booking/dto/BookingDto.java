package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class BookingDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_date")
    Timestamp start_date;

    @Column(name = "end_date")
    Timestamp end_date;

    @JoinColumn(name = "item_id")
    @ManyToOne
    ItemDto item;

    @JoinColumn(name = "booker_id")
    @ManyToOne
    UserDto booker;

    @Enumerated(value = EnumType.STRING)
    BookingStatus status;

}
