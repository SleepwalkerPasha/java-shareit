package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest addRequest(long userId, ItemRequest request);

    List<ItemRequest> getAllRequestsByUserId(long userId);

    List<ItemRequest> getAllRequests(long userId, long from, long size);

    ItemRequest getRequestById(long userId, long requestId);
}
