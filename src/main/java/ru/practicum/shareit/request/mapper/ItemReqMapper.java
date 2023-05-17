package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.sql.Timestamp;

public class ItemReqMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        if (itemRequestDto.getId() != null)
            itemRequest.setId(itemRequestDto.getId());
        if (itemRequestDto.getDescription() != null)
            itemRequest.setDescription(itemRequestDto.getDescription());
        if (itemRequestDto.getCreated() != null)
            itemRequest.setCreated(itemRequestDto.getCreated().toInstant());
        if (itemRequestDto.getRequestor() != null)
            itemRequest.setRequestor(UserMapper.toUser(itemRequestDto.getRequestor()));
        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        if (itemRequest.getId() != null)
            itemRequestDto.setId(itemRequest.getId());
        if (itemRequest.getDescription() != null)
            itemRequestDto.setDescription(itemRequest.getDescription());
        if (itemRequest.getCreated() != null)
            itemRequestDto.setCreated(Timestamp.from(itemRequest.getCreated()));
        if (itemRequest.getRequestor() != null)
            itemRequestDto.setRequestor(UserMapper.toUserDto(itemRequest.getRequestor()));
        return itemRequestDto;
    }
}
