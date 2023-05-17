package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<ItemDto, Long> {

    @Query("select i from ItemDto i where lower(i.name) like lower(concat('%', ?1,'%')) " +
            "or lower(i.description) like lower(concat('%', ?1,'%'))")
    List<ItemDto> findAllItemsBySubstring(String substr);

    @Query("select i from ItemDto i " +
            "join i.owner as u " +
            "where u.id = ?1")
    List<ItemDto> findAllUserItemsByUserId(long userId);
}
