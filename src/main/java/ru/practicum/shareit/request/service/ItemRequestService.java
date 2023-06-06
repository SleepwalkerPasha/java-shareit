package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest addRequest(long userId, ItemRequest request);

    List<ItemRequest> getAllRequestsByOwnerId(long userId);

    List<ItemRequest> getAllRequests(long userId, Integer from, Integer size);

    ItemRequest getRequestById(long userId, long requestId);
}
