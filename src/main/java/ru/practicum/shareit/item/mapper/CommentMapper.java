package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        if (commentDto.getId() != null) {
            comment.setId(commentDto.getId());
        }
        if (commentDto.getItemDto() != null) {
            comment.setItem(ItemMapper.toItem(commentDto.getItemDto()));
        }
        if (commentDto.getAuthorDto() != null) {
            comment.setAuthorName(commentDto.getAuthorDto().getName());
        }
        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }
        if (commentDto.getCreated() != null) {
            comment.setCreated(commentDto.getCreated());
        }
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        if (comment.getId() != null) {
            commentDto.setId(comment.getId());
        }
        if (comment.getItem() != null) {
            commentDto.setItemDto(ItemMapper.toItemDto(comment.getItem()));
        }
        if (comment.getText() != null) {
            commentDto.setText(comment.getText());
        }
        if (comment.getCreated() != null) {
            commentDto.setCreated(comment.getCreated());
        }
        return commentDto;
    }

}
