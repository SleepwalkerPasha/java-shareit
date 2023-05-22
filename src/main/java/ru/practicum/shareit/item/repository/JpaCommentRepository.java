package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<CommentDto, Long> {

    List<CommentDto> findAllByItemDto_Id(long itemId);

    @Query("select c from CommentDto c " +
            "where c.itemDto.id in ?1")
    List<CommentDto> findAllInItemIds(List<Long> itemIds);
}
