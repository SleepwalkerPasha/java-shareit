package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
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
    public List<ItemDto> getItemsByDescription(String description) {
        return null;
    }
}
