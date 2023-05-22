package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponse {
    Long id;

    @NotNull(groups = BasicInfo.class)
    @NotBlank(groups = BasicInfo.class)
    String name;

    @NotNull(groups = BasicInfo.class)
    @NotBlank(groups = BasicInfo.class)
    String description;

    @NotNull(groups = BasicInfo.class)
    Boolean available;

    BookingInfo lastBooking;

    BookingInfo nextBooking;

    List<Comment> comments;
}
