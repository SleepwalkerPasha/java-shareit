package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
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

    @Override
    public Item addItem(Item item, long userId) {
        item.setOwner(userId);
        UserDto userDto = checkForUser(userId);
        ItemDto itemDto = itemRepository.addItem(ItemMapper.toItemDto(item));
        userDto.addItem(itemDto);
        return ItemMapper.toItem(itemDto);
    }

    @Override
    public Item updateItem(Item item, long itemId, long userId) {
        checkNewItem(item);
        UserDto userDto = checkForUser(userId);
        Optional<ItemDto> oldItemOpt = userDto.getItemById(itemId);

        if (oldItemOpt.isEmpty())
            throw new NotFoundException(String.format("итема с таким id = %d нет у юзера %d", itemId, userId));
        if (!oldItemOpt.get().getOwner().equals(userId))
            throw new ConflictException("у этого юзера нет прав для изменения этого итема");

        ItemDto itemDto = updateItemsValues(ItemMapper.toItemDto(item), oldItemOpt.get());
        userDto.updateItem(itemDto);
        itemRepository.updateItem(itemDto);
        return ItemMapper.toItem(itemDto);
    }

    private void checkNewItem(Item item) {
        if ((item.getName() != null && item.getName().isBlank()) || (item.getDescription() != null && item.getDescription().isBlank()))
            throw new IllegalArgumentException("описание и название итема не могут быть пустыми");
    }

    @Override
    public Optional<Item> getItem(long itemId, long userId) {
        return Optional.of(ItemMapper.toItem(itemRepository.getItem(itemId)));
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        UserDto userDto = checkForUser(userId);
        return userDto
                .getItems()
                .stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());
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
