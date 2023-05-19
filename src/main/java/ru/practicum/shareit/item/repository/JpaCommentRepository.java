package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<CommentDto, Long> {

    List<CommentDto> findAllByItemDto_Id(long itemId);

    List<CommentDto> findAllByAuthorDto_Id(long userId);
}
