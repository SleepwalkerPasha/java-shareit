package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    ItemDto addItem(ItemDto item);

    ItemDto updateItem(ItemDto item);

    Optional<ItemDto> getItem(long itemId);

    List<ItemDto> getItemsByDescription(String description);

    List<ItemDto> getUserItemsByUserId(long userId);
}
