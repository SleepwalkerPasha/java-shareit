package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    @Override
    public Item addItem(Item item, long userId) {
        UserDto userDto = checkForUser(userId);
        item.setOwner(UserMapper.toUser(userDto));
        ItemDto itemDto = itemRepository.addItem(ItemMapper.toItemDto(item));
        return ItemMapper.toItem(itemDto);
    }

    @Override
    @Transactional
    public Item updateItem(Item item, long itemId, long userId) {
        checkNewItem(item);
        checkForUser(userId);
        Optional<ItemDto> oldItemOpt = itemRepository.getItem(itemId);

        if (oldItemOpt.isEmpty())
            throw new NotFoundException(String.format("итема с таким id = %d нет у юзера %d", itemId, userId));
        if (!oldItemOpt.get().getOwner().getId().equals(userId))
            throw new ConflictException("у этого юзера нет прав для изменения этого итема");

        ItemDto itemDto = updateItemsValues(ItemMapper.toItemDto(item), oldItemOpt.get());
        itemRepository.updateItem(itemDto);
        return ItemMapper.toItem(itemDto);
    }

    private void checkNewItem(Item item) {
        if ((item.getName() != null && item.getName().isBlank()) || (item.getDescription() != null && item.getDescription().isBlank()))
            throw new IllegalArgumentException("описание и название итема не могут быть пустыми");
    }

    @Override
    @Transactional
    public Item getItem(long itemId, long userId) {
        Optional<ItemDto> itemDto = itemRepository.getItem(itemId);
        if (itemDto.isEmpty())
            throw new NotFoundException(String.format("итема с таким id = %d нет", itemId));
        List<Comment> commentByItemId = commentRepository.getCommentByItemId(itemId);
        Item item = ItemMapper.toItem(itemDto.get());
        item.setComments(commentByItemId);
        return item;
    }

    @Override
    @Transactional
    public List<Item> getAllUserItems(long userId) {
        checkForUser(userId);
        List<Item> items = itemRepository.getUserItemsByUserId(userId)
                .stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());
        for (Item item : items) {
            List<Comment> commentByItemId = commentRepository.getCommentByItemId(item.getId());
            item.setComments(commentByItemId);
        }
        return items;
    }

    @Override
    public List<Item> getItemByDescription(String description, long userId) {
        if (description.isBlank())
            return new ArrayList<>();
        return itemRepository
                .getItemsByDescription(description.toLowerCase())
                .stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());
    }

    @Override
    public Comment addCommentToItem(long itemId, long userId, Comment comment) {
        UserDto userDto = checkForUser(userId);
        Optional<BookingDto> bookingByItemIdAndUserId = bookingRepository.getBookingByItemIdAndUserId(itemId, userId);
        if (bookingByItemIdAndUserId.isEmpty())
            throw new NotFoundException("данный пользователь не брал эту вещь в аренду");
        comment.setAuthor(UserMapper.toUser(userDto));
        Optional<ItemDto> item = itemRepository.getItem(itemId);
        if (item.isEmpty())
            throw new NotFoundException("такого итема нет");
        comment.setItem(ItemMapper.toItem(item.get()));
        commentRepository.addComment(comment);
        return null;
    }

    private ItemDto updateItemsValues(ItemDto newItem, ItemDto oldItem) {
        if (newItem.getDescription() != null && !newItem.getDescription().equals(oldItem.getDescription()))
            oldItem.setDescription(newItem.getDescription());
        if (newItem.getName() != null && !newItem.getName().equals(oldItem.getName()))
            oldItem.setName(newItem.getName());
        if (newItem.getAvailable() != null && !newItem.getAvailable().equals(oldItem.getAvailable()))
            oldItem.setAvailable(newItem.getAvailable());
        return oldItem;
    }

    private UserDto checkForUser(long userId) {
        Optional<UserDto> userById = userRepository.getUserById(userId);
        if (userById.isEmpty())
            throw new NotFoundException("такого пользователя нет");
        else
            return userById.get();
    }
}
