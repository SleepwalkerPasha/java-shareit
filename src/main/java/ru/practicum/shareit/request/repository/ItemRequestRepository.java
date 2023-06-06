package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository {

    ItemRequestDto addRequest(ItemRequestDto requestDto);

    List<ItemRequestDto> getAllRequestsByUserId(long userId);

    Page<ItemRequestDto> getAllRequests(long userId, Pageable pageable);

    Optional<ItemRequestDto> getRequestById(long requestId);
}
