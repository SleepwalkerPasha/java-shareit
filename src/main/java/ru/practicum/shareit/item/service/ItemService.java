package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(Item itemDto, long userId);

    Item updateItem(Item itemDto, long itemId, long userId);

    Item getItem(long itemId, long userId);

    List<Item> getAllUserItems(long userId);

    List<Item> getItemByDescription(String description, long userId);

    Comment addCommentToItem(long itemId, long userId, Comment comment);
}
