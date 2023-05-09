package ru.practicum.ewm.entity.event.mapper.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.entity.event.dto.request.comment.AddCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.entity.comment.Comment;
import ru.practicum.ewm.entity.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public final class CommentMapper {

    public static Comment toComment(AddCommentRequestDto commentDto, User author, Event event) {
        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        comment.setEvent(event);

        return comment;
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto commentDto = new CommentResponseDto();

        commentDto.setId(comment.getId());
        commentDto.setAuthorId(comment.getAuthor().getId());
        commentDto.setEventId(comment.getEvent().getId());
        commentDto.setText(comment.getText());

        return commentDto;
    }

    public static List<CommentResponseDto> toCommentResponseDto(Iterable<Comment> comments) {
        return StreamSupport.stream(comments.spliterator(), false)
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.toList());
    }
}
