package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemBookingInfo;

import java.util.List;

public interface ItemService {

    Item addItem(Item itemDto, long userId);

    Item updateItem(Item itemDto, long itemId, long userId);

    ItemBookingInfo getItem(long itemId, long userId);

    List<ItemBookingInfo> getAllUserItems(long userId, int from, int size);

    List<Item> getItemByDescription(String description, long userId, int from, int size);

    Comment addCommentToItem(long itemId, long userId, Comment comment);
}
