package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        return CommentMapper.toComment(commentRepository.save(CommentMapper.toCommentDto(comment)));
    }

    @Override
    public List<Comment> getCommentByAuthorId(long authorId) {
        return commentRepository.findAllByAuthorDto_Id(authorId).stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> getCommentByItemId(long itemId) {
        return commentRepository.findAllByItemDto_Id(itemId).stream()
                .map(CommentMapper::toComment)
                .collect(Collectors.toList());
    }
}
