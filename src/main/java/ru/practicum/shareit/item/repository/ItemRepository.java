package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Optional;

public interface ItemRepository {

    ItemDto addItem(ItemDto item);

    ItemDto updateItem(ItemDto item);

    Optional<ItemDto> getItem(long itemId);

    Page<ItemDto> getItemsByDescription(String description, Pageable pageable);

    Page<ItemDto> getUserItemsByUserId(long userId, Pageable pageable);
}
