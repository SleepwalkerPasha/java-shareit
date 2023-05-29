package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface JpaItemRequestRepository extends JpaRepository<ItemRequestDto, Long> {

    List<ItemRequestDto> findAllByRequestor_IdOrderByCreatedDesc(long userId);

    @Query("select ir from ItemRequestDto ir " +
            "where ir.requestor.id != ?1 " +
            "order by ir.created desc")
    Page<ItemRequestDto> findAllItemRequests(long userId, Pageable pageable);

    @Query("select ir from ItemRequestDto ir " +
            "where ir.requestor.id != ?1 " +
            "order by ir.created desc")
    List<ItemRequestDto> findAllItemRequests(long userId);
}
