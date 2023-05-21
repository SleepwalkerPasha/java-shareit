package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.repository.JpaUserRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository commentRepository;

    private final JpaUserRepository userRepository;

    @Override
    public CommentDto addComment(CommentDto comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> getCommentByItemId(long itemId) {
        return commentRepository.findAllByItemDto_Id(itemId);
    }
}
