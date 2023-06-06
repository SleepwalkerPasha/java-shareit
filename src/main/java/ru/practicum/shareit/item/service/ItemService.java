package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingInfo;

import java.util.List;

public interface ItemService {

    Item addItem(Item item, long userId);

    Item updateItem(Item item, long itemId, long userId);

    ItemBookingInfo getItem(long itemId, long userId);

    List<ItemBookingInfo> getAllUserItems(long userId, Integer from, Integer size);

    List<Item> getItemsByDescription(String description, long userId, Integer from, Integer size);

    Comment addCommentToItem(long itemId, long userId, Comment comment);
}
