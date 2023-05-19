package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository {

    Comment addComment(Comment comment);

    List<Comment> getCommentByAuthorId(long authorId);

    List<Comment> getCommentByItemId(long itemId);
}
