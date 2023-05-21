package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.BookingInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemResponse;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    @Transactional(readOnly = true)
    public ItemResponse getItem(long itemId, long userId) {
        Optional<ItemDto> itemDto = itemRepository.getItem(itemId);
        if (itemDto.isEmpty())
            throw new NotFoundException(String.format("итема с таким id = %d нет", itemId));
        List<Comment> commentByItemId = commentRepository.getCommentByItemId(itemId)
                .stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());
        ItemResponse itemResponse = ItemMapper.toItemResponse(itemDto.get());
        if (itemDto.get().getOwner().getId().equals(userId)) {
            setBookingsForItemResponse(itemResponse);
        }
        itemResponse.setComments(commentByItemId);
        return itemResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getAllUserItems(long userId) {
        checkForUser(userId);
        List<ItemDto> items = itemRepository.getUserItemsByUserId(userId);
        List<ItemResponse> itemResponses = new ArrayList<>();
        for (ItemDto itemDto : items) {
            ItemResponse itemResponse = ItemMapper.toItemResponse(itemDto);
            List<Comment> commentByItemId = commentRepository.getCommentByItemId(itemDto.getId())
                    .stream()
                    .map(CommentMapper::toComment)
                    .collect(Collectors.toList());
            if (itemDto.getOwner().getId().equals(userId)) {
                setBookingsForItemResponse(itemResponse);
            }
            itemResponse.setComments(commentByItemId);
            itemResponses.add(itemResponse);
        }
        return itemResponses;
    }

    private void setBookingsForItemResponse(ItemResponse itemResponse) {
        List<BookingDto> bookings = bookingRepository.getBookingsByItemId(itemResponse.getId());

        Optional<BookingDto> nextBooking = bookings
                .stream()
                .filter(x -> x.getStart_date().after(Timestamp.valueOf(LocalDateTime.now())) && x.getStatus().equals(BookingStatus.APPROVED))
                .findFirst();
        Collections.reverse(bookings);
        Optional<BookingDto> lastBooking = bookings
                .stream()
                .filter(x -> x.getStart_date().before(Timestamp.valueOf(LocalDateTime.now())) && x.getStatus().equals(BookingStatus.APPROVED))
                .findFirst();
        nextBooking.ifPresent(bookingDto -> itemResponse.setNextBooking(new BookingInfo(bookingDto.getId(), bookingDto.getBooker().getId())));
        lastBooking.ifPresent(bookingDto -> itemResponse.setLastBooking(new BookingInfo(bookingDto.getId(), bookingDto.getBooker().getId())));
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
        Optional<ItemDto> item = itemRepository.getItem(itemId);
        if (item.isEmpty())
            throw new NotFoundException("такого итема нет");
        List<BookingDto> bookings = bookingRepository.getBookingsByItemIdAndUserId(itemId, userId);
        if (bookings.isEmpty()) {
            throw new UnavailableItemException("данный пользователь не брал эту вещь в аренду");
        }
        if (bookings.size() == 1 && bookings.get(0).getEnd_date().after(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new UnsupportedStatusException("бронирование еще не началось");
        }
        comment.setAuthorName(userDto.getName());
        comment.setItem(ItemMapper.toItem(item.get()));
        comment.setCreated(LocalDateTime.now());
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        commentDto.setAuthorDto(userDto);
        return CommentMapper.toComment(commentRepository.addComment(commentDto));
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
