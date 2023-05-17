package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemReqMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemMapper {

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        if (itemDto.getId() != null)
            item.setId(itemDto.getId());
        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());
        if (itemDto.getItemRequest() != null)
            item.setRequest(ItemReqMapper.toItemRequest(itemDto.getItemRequest()));
        if (itemDto.getAvailable() != null)
            item.setAvailable(itemDto.getAvailable());
        if (itemDto.getOwner() != null)
            item.setOwner(UserMapper.toUser(itemDto.getOwner()));
        if (itemDto.getName() != null)
            item.setName(itemDto.getName());
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        if (item.getId() != null)
            itemDto.setId(item.getId());
        if (item.getDescription() != null)
            itemDto.setDescription(item.getDescription());
        if (item.getRequest() != null)
            itemDto.setItemRequest(ItemReqMapper.toItemRequestDto(item.getRequest()));
        if (item.getAvailable() != null)
            itemDto.setAvailable(item.getAvailable());
        if (item.getOwner() != null)
            itemDto.setOwner(UserMapper.toUserDto(item.getOwner()));
        if (item.getName() != null)
            itemDto.setName(item.getName());
        return itemDto;
    }
}
