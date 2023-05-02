package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final HashMap<Long, ItemDto> itemMap = new HashMap<>();

    private long counter = 0L;

    @Override
    public ItemDto addItem(ItemDto item) {
        item.setId(++counter);
        itemMap.putIfAbsent(item.getId(), item);
        return item;
    }

    @Override
    public ItemDto updateItem(ItemDto item) {
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public ItemDto getItem(long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<ItemDto> getItemsByDescription(String description) {
        return itemMap
                .values()
                .stream()
                .filter(x -> x.getDescription().toLowerCase().contains(description) && x.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

}
