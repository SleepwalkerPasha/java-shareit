package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {

    ItemDto addItem(ItemDto item);

    ItemDto updateItem(ItemDto item);

    ItemDto getItem(long item);

    List<ItemDto> getItemsByDescription(String description);
}
