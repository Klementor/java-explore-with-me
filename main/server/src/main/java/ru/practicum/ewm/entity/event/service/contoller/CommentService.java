package ru.practicum.ewm.entity.event.service.contoller;

import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;

public interface CommentService {
    Iterable<CommentResponseDto> getComments(Long id, Integer from, Integer size);

    CommentResponseDto getCommentById(Long id, Long comId);
}
