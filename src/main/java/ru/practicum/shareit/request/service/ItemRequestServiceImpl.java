package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest addRequest(long userId, ItemRequest request) {
        return null;
    }

    @Override
    public List<ItemRequest> getAllRequestsByUserId(long userId) {
        return null;
    }

    @Override
    public List<ItemRequest> getAllRequests(long userId, long from, long size) {
        return null;
    }

    @Override
    public ItemRequest getRequestById(long userId, long requestId) {
        return null;
    }
}
