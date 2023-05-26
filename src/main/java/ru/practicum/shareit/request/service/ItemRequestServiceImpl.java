package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemReqMapper;
import ru.practicum.shareit.request.model.ItemRequest;
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
    public List<ItemRequest> getAllRequestsByUserId(long userId) {
        checkForUser(userId);
        return itemRequestRepository.getAllRequestsByUserId(userId)
                .stream()
                .map(ItemReqMapper::toItemRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequest> getAllRequests(long userId, Integer from, Integer size) {
        checkForUser(userId);
        if (from != null && size != null) {
            Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
            return itemRequestRepository.getAllRequests(userId, pageable)
                    .stream()
                    .map(ItemReqMapper::toItemRequest)
                    .collect(Collectors.toList());
        } else {
            return itemRequestRepository.getAllRequests(userId)
                    .stream()
                    .map(ItemReqMapper::toItemRequest)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemRequest getRequestById(long userId, long requestId) {
        checkForUser(userId);
        Optional<ItemRequestDto> requestById = itemRequestRepository.getRequestById(userId, requestId);
        if (requestById.isEmpty())
            throw new NotFoundException("такого запроса не существует");
        return ItemReqMapper.toItemRequest(requestById.get());
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
