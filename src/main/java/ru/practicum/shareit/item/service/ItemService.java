package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item addItem(Item itemDto, long userId);

    Item updateItem(Item itemDto, long itemId, long userId);

    Optional<Item> getItem(long itemId, long userId);

    List<Item> getAllUserItems(long userId);

    List<Item> getItemByDescription(String description, long userId);
}
