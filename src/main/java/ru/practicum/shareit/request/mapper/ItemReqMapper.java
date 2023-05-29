package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemResponse;
import ru.practicum.shareit.user.mapper.UserMapper;

public class ItemReqMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        if (itemRequestDto.getId() != null)
            itemRequest.setId(itemRequestDto.getId());
        if (itemRequestDto.getDescription() != null)
            itemRequest.setDescription(itemRequestDto.getDescription());
        if (itemRequestDto.getCreated() != null)
            itemRequest.setCreated(itemRequestDto.getCreated());
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
            itemRequestDto.setCreated(itemRequest.getCreated());
        if (itemRequest.getRequestor() != null)
            itemRequestDto.setRequestor(UserMapper.toUserDto(itemRequest.getRequestor()));
        return itemRequestDto;
    }

    public static ItemResponse toItemResponse(ItemDto itemDto) {
        ItemResponse itemResponse = new ItemResponse();
        if (itemDto.getId() != null) {
            itemResponse.setId(itemDto.getId());
        }
        if (itemDto.getName() != null) {
            itemResponse.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemResponse.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemResponse.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getItemRequest() != null) {
            itemResponse.setRequestId(itemDto.getItemRequest().getId());
        }
        if (itemDto.getOwner() != null) {
            itemResponse.setOwner(UserMapper.toUser(itemDto.getOwner()));
        }
        return itemResponse;
    }
}
