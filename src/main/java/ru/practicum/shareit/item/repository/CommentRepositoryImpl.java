package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.CommentDto;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository commentRepository;

    @Override
    public CommentDto addComment(CommentDto comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> getCommentsByItemId(long itemId) {
        return commentRepository.findAllByItemDto_Id(itemId);
    }

    @Override
    public List<CommentDto> getCommentsInItemIds(List<Long> itemIds) {
        return commentRepository.findAllInItemIds(itemIds);
    }

}
