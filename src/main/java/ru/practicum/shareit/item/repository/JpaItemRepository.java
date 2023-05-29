package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface JpaItemRepository extends JpaRepository<ItemDto, Long> {

    @Query("select i from ItemDto i where (lower(i.name) like lower(concat('%', ?1,'%')) " +
            "or lower(i.description) like lower(concat('%', ?1,'%'))) and i.available = true")
    Page<ItemDto> findAllItemsBySubstring(String substr, Pageable pageable);

    @Query("select i from ItemDto i " +
            "join i.owner as u " +
            "where u.id = ?1")
    Page<ItemDto> findAllUserItemsByUserId(long userId, Pageable pageable);

    @Query("select i from ItemDto i where (lower(i.name) like lower(concat('%', ?1,'%')) " +
            "or lower(i.description) like lower(concat('%', ?1,'%'))) and i.available = true")
    List<ItemDto> findAllItemsBySubstring(String substr);

    @Query("select i from ItemDto i " +
            "join i.owner as u " +
            "where u.id = ?1")
    List<ItemDto> findAllUserItemsByUserId(long userId);

    List<ItemDto> findAllByItemRequest_Id(long requestId);

    @Query("select i from ItemDto i " +
            "where i.itemRequest.id in ?1")
    List<ItemDto> findAllInItemRequests(List<Long> requestIds);
}
