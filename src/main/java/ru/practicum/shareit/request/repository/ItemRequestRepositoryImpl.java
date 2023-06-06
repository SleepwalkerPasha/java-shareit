package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ItemRequestRepositoryImpl implements ItemRequestRepository {

    private final JpaItemRequestRepository jpaItemRequestRepository;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto) {
        return jpaItemRequestRepository.save(requestDto);
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByUserId(long userId) {
        return jpaItemRequestRepository.findAllByRequestor_IdOrderByCreatedDesc(userId);
    }

    @Override
    public Page<ItemRequestDto> getAllRequests(long userId, Pageable pageable) {
        return jpaItemRequestRepository.findAllItemRequests(userId, pageable);
    }


    @Override
    public Optional<ItemRequestDto> getRequestById(long requestId) {
        return jpaItemRequestRepository.findById(requestId);
    }

}
