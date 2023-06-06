package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemReqMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemResponse;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemRequest addRequest(long userId, ItemRequest request) {
        UserDto requestor = checkForUser(userId);
        ItemRequestDto itemRequestDto = ItemReqMapper.toItemRequestDto(request);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(requestor);
        return ItemReqMapper.toItemRequest(itemRequestRepository.addRequest(itemRequestDto));
    }

    @Override
    public List<ItemRequest> getAllRequestsByOwnerId(long userId) {
        checkForUser(userId);
        List<ItemRequest> requests = itemRequestRepository.getAllRequestsByUserId(userId)
                .stream()
                .map(ItemReqMapper::toItemRequest)
                .collect(Collectors.toList());
        setItemsForItemRequest(requests);
        return requests;
    }

    @Override
    public List<ItemRequest> getAllRequests(long userId, Integer from, Integer size) {
        checkForUser(userId);
        List<ItemRequest> itemRequests;
        Pageable pageable = ItemServiceImpl.PageRequester.of(from, size, Sort.by("created").descending());
        itemRequests = itemRequestRepository.getAllRequests(userId, pageable)
                .stream()
                .map(ItemReqMapper::toItemRequest)
                .collect(Collectors.toList());
        setItemsForItemRequest(itemRequests);
        return itemRequests;
    }

    @Override
    public ItemRequest getRequestById(long userId, long requestId) {
        checkForUser(userId);
        Optional<ItemRequestDto> requestById = itemRequestRepository.getRequestById(requestId);
        if (requestById.isEmpty())
            throw new NotFoundException("такого запроса не существует");
        List<ItemResponse> items = itemRepository.getItemsByItemRequestId(requestId)
                .stream()
                .map(ItemReqMapper::toItemResponse)
                .collect(Collectors.toList());
        ItemRequest request = ItemReqMapper.toItemRequest(requestById.get());
        request.setItems(items);
        return request;
    }

    private void setItemsForItemRequest(List<ItemRequest> itemRequests) {
        if (itemRequests.isEmpty()) {
            return;
        }
        List<Long> requestsIds = itemRequests.stream().map(ItemRequest::getId).collect(Collectors.toList());
        List<ItemDto> items = itemRepository.getItemsInRequestIds(requestsIds);
        for (ItemRequest itemRequest : itemRequests) {
            List<ItemResponse> itemsForRequest = items
                    .stream()
                    .filter(x -> ItemReqMapper.toItemRequest(x.getItemRequest()).equals(itemRequest))
                    .map(ItemReqMapper::toItemResponse)
                    .collect(Collectors.toList());
            itemRequest.setItems(itemsForRequest);
        }
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
