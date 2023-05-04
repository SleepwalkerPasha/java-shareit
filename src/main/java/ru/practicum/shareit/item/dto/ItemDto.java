package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    Long owner;

    ItemRequest itemRequest;
}
