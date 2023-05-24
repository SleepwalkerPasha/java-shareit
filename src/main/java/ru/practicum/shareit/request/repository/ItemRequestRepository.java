package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository {

    ItemRequestDto addRequest(ItemRequestDto requestDto);

    List<ItemRequestDto> getAllRequestsByUserId(long userId);

    List<ItemRequestDto> getAllRequests(long userId, long from, long size);

    Optional<ItemRequestDto> getRequestById(long userId, long requestId);
}
