package ru.practicum.ewm.entity.event.service.contoller;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.event.dto.request.comment.AddCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.request.comment.UpdateCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;

public interface CommentAdminService {
    @Transactional
    CommentResponseDto addComment(Long userId, Long eventId, AddCommentRequestDto commentDto);

    @Transactional
    CommentResponseDto updateCommentById(
            Long userId,
            Long eventId,
            Long comId,
            UpdateCommentRequestDto commentDto
    );

    @Transactional
    void deleteCommentById(Long userId, Long eventId, Long comId);
}
