package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
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
import ru.practicum.shareit.item.model.ItemBookingInfo;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

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

    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Item addItem(Item item, long userId) {
        UserDto userDto = checkForUser(userId);
        item.setOwner(UserMapper.toUser(userDto));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        Long requestId = item.getRequestId();
        if (requestId != null) {
            Optional<ItemRequestDto> requestById = itemRequestRepository.getRequestById(userId, requestId);
            if (requestById.isEmpty()) {
                throw new NotFoundException("такого запроса на вещь нет");
            }
            itemDto.setItemRequest(requestById.get());
        }
        return ItemMapper.toItem(itemRepository.addItem(itemDto));
    }

    @Override
    @Transactional
    public Item updateItem(Item item, long itemId, long userId) {
        checkNewItem(item);
        checkForUser(userId);
        Optional<ItemDto> oldItemOpt = itemRepository.getItem(itemId);

        if (oldItemOpt.isEmpty()) {
            throw new NotFoundException(String.format("итема с таким id = %d нет у юзера %d", itemId, userId));
        }
        if (!oldItemOpt.get().getOwner().getId().equals(userId)) {
            throw new ConflictException("у этого юзера нет прав для изменения этого итема");
        }

        ItemDto itemDto = updateItemsValues(ItemMapper.toItemDto(item), oldItemOpt.get());
        itemRepository.updateItem(itemDto);
        return ItemMapper.toItem(itemDto);
    }

    private void checkNewItem(Item item) {
        if ((item.getName() != null && item.getName().isBlank())
                || (item.getDescription() != null && item.getDescription().isBlank())) {
            throw new IllegalArgumentException("описание и название итема не могут быть пустыми");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ItemBookingInfo getItem(long itemId, long userId) {
        Optional<ItemDto> itemDto = itemRepository.getItem(itemId);
        if (itemDto.isEmpty()) {
            throw new NotFoundException(String.format("итема с таким id = %d нет", itemId));
        }
        List<Comment> commentByItemId = commentRepository.getCommentsByItemId(itemId)
                .stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());
        ItemBookingInfo itemBookingInfo = ItemMapper.toItemResponse(itemDto.get());
        if (itemDto.get().getOwner().getId().equals(userId)) {
            List<BookingDto> bookings = bookingRepository.getApprovedBookingsByItemId(itemBookingInfo.getId());
            setBookingsForItemResponse(itemBookingInfo, bookings);
        }
        itemBookingInfo.setComments(commentByItemId);
        return itemBookingInfo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemBookingInfo> getAllUserItems(long userId, Integer from, Integer size) {
        checkForUser(userId);
        Pageable pageable = null;
        if (from != null && size != null) {
            pageable = PageRequest.of(from, size);
        }
        List<ItemDto> items;
        if (pageable != null) {
            items = itemRepository.getUserItemsByUserId(userId, pageable)
                    .stream()
                    .collect(Collectors.toList());
        } else {
            items = itemRepository.getUserItemsByUserId(userId);
        }
        List<ItemBookingInfo> itemResponseList = new ArrayList<>();
        List<Long> itemIds = items.stream().map(ItemDto::getId).collect(Collectors.toList());

        List<BookingDto> bookingDtos = bookingRepository.getApprovedBookingsInItems(itemIds);
        List<CommentDto> commentDtos = commentRepository.getCommentsInItemIds(itemIds);

        for (ItemDto itemDto : items) {
            ItemBookingInfo itemBookingInfo = ItemMapper.toItemResponse(itemDto);
            List<Comment> commentByItemId = commentDtos
                    .stream()
                    .filter(x -> x.getItemDto().getId().equals(itemDto.getId()))
                    .map(CommentMapper::toComment)
                    .collect(Collectors.toList());

            if (itemDto.getOwner().getId().equals(userId)) {
                List<BookingDto> bookings = bookingDtos
                        .stream()
                        .filter(x -> x.getItem().getId().equals(itemDto.getId()))
                        .collect(Collectors.toList());
                setBookingsForItemResponse(itemBookingInfo, bookings);
            }
            itemBookingInfo.setComments(commentByItemId);
            itemResponseList.add(itemBookingInfo);
        }
        return itemResponseList;
    }

    private void setBookingsForItemResponse(ItemBookingInfo itemBookingInfo, List<BookingDto> bookings) {
        Optional<BookingDto> nextBooking = bookings
                .stream()
                .filter(x -> x.getStartDate().isAfter(LocalDateTime.now()))
                .findFirst();
        Collections.reverse(bookings);
        Optional<BookingDto> lastBooking = bookings
                .stream()
                .filter(x -> x.getStartDate().isBefore(LocalDateTime.now()))
                .findFirst();
        nextBooking
                .ifPresent(bookingDto ->
                        itemBookingInfo
                                .setNextBooking(new BookingInfo(bookingDto.getId(), bookingDto.getBooker().getId())));
        lastBooking
                .ifPresent(bookingDto ->
                        itemBookingInfo
                                .setLastBooking(new BookingInfo(bookingDto.getId(), bookingDto.getBooker().getId())));
    }

    @Override
    public List<Item> getItemByDescription(String description, long userId, Integer from, Integer size) {
        Pageable pageable = null;
        if (from != null && size != null) {
            pageable = PageRequest.of(from, size);
        }

        if (description.isBlank()) {
            return new ArrayList<>();
        }
        if (pageable != null) {
            return itemRepository
                    .getItemsByDescription(description.toLowerCase(), pageable)
                    .stream()
                    .map(ItemMapper::toItem)
                    .collect(Collectors.toList());
        } else {
            return itemRepository.getItemsByDescription(description.toLowerCase())
                    .stream()
                    .map(ItemMapper::toItem)
                    .collect(Collectors.toList());
        }
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
        int count = 0;
        for (BookingDto booking : bookings) {
            if (booking.getEndDate().isAfter(LocalDateTime.now())) {
                count++;
            }
        }
        if (count == bookings.size()) {
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
        if (newItem.getDescription() != null && !newItem.getDescription().equals(oldItem.getDescription())) {
            oldItem.setDescription(newItem.getDescription());
        }
        if (newItem.getName() != null && !newItem.getName().equals(oldItem.getName())) {
            oldItem.setName(newItem.getName());
        }
        if (newItem.getAvailable() != null && !newItem.getAvailable().equals(oldItem.getAvailable())) {
            oldItem.setAvailable(newItem.getAvailable());
        }
        return oldItem;
    }

    private UserDto checkForUser(long userId) {
        Optional<UserDto> userById = userRepository.getUserById(userId);
        if (userById.isEmpty()) {
            throw new NotFoundException("такого пользователя нет");
        } else {
            return userById.get();
        }
    }
}
