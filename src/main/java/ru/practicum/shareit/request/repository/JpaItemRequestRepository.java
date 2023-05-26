package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface JpaItemRequestRepository extends JpaRepository<ItemRequestDto, Long> {

    List<ItemRequestDto> findAllByRequestor_IdOrderByCreatedDesc(long userId);

    Page<ItemRequestDto> findAllItemRequests(Pageable pageable);

    @Query("select ir from ItemRequestDto ir " +
            "order by ir.created desc")
    List<ItemRequestDto> findAllItemRequests();
}
