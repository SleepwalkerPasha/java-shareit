package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;

public interface JpaItemRepository extends JpaRepository<ItemDto, Long> {
}
