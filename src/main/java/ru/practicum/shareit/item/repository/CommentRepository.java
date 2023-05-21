package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

public interface CommentRepository {

    CommentDto addComment(CommentDto comment);

    List<CommentDto> getCommentByItemId(long itemId);
}
