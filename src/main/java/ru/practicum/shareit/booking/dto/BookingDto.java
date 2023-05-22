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
import java.time.LocalDateTime;

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
    LocalDateTime startDate;

    @Column(name = "end_date")
    LocalDateTime endDate;

    @JoinColumn(name = "item_id")
    @ManyToOne(targetEntity = ItemDto.class, fetch = FetchType.LAZY)
    ItemDto item;

    @JoinColumn(name = "booker_id")
    @ManyToOne(targetEntity = UserDto.class, fetch = FetchType.LAZY)
    UserDto booker;

    @Enumerated(value = EnumType.STRING)
    BookingStatus status;

}
