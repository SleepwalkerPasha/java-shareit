package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final JpaItemRepository jpaItemRepository;

    @Override
    public ItemDto addItem(ItemDto item) {
        return jpaItemRepository.save(item);
    }

    @Override
    public ItemDto updateItem(ItemDto item) {
        return jpaItemRepository.save(item);
    }

    @Override
    public Optional<ItemDto> getItem(long itemId) {
        return jpaItemRepository.findById(itemId);
    }

    @Override
    public Page<ItemDto> getItemsByDescription(String description, Pageable pageable) {
        return jpaItemRepository.findAllItemsBySubstring(description, pageable);
    }

    @Override
    public Page<ItemDto> getUserItemsByUserId(long userId, Pageable pageable) {
        return jpaItemRepository.findAllUserItemsByUserId(userId, pageable);
    }

    @Override
    public List<ItemDto> getItemsByItemRequestId(long requestId) {
        return jpaItemRepository.findAllByItemRequest_Id(requestId);
    }

    @Override
    public List<ItemDto> getItemsInRequestIds(List<Long> requestIds) {
        return jpaItemRepository.findAllInItemRequests(requestIds);
    }
}
