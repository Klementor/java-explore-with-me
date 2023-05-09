package ru.practicum.ewm.entity.event.service.contoller.impl.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.event.dto.request.comment.AddCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.request.comment.UpdateCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.entity.comment.Comment;
import ru.practicum.ewm.entity.event.mapper.comment.CommentMapper;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.event.repository.comment.CommentJpaRepository;
import ru.practicum.ewm.entity.event.service.contoller.CommentAdminService;
import ru.practicum.ewm.entity.user.entity.User;
import ru.practicum.ewm.entity.user.repository.UserJpaRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements CommentAdminService {

    private final EventJpaRepository eventRepository;
    private final UserJpaRepository userRepository;
    private final CommentJpaRepository commentRepository;

    @Override
    @Transactional
    public CommentResponseDto addComment(Long userId, Long eventId, AddCommentRequestDto commentDto) {
        userRepository.checkUserExistsById(userId);
        eventRepository.checkEventExistsById(eventId);
        Comment comment = getComment(commentDto, userId, eventId);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentResponseDto(savedComment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateCommentById(
            Long userId,
            Long eventId,
            Long comId,
            UpdateCommentRequestDto commentDto
    ) {
        userRepository.checkUserExistsById(userId);
        eventRepository.checkEventExistsById(eventId);
        commentRepository.checkCommentExistsById(comId);
        Comment updatedComment = getUpdatedComment(comId, commentDto);
        Comment savedComment = commentRepository.save(updatedComment);
        return CommentMapper.toCommentResponseDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentById(Long userId, Long eventId, Long comId) {
        userRepository.checkUserExistsById(userId);
        eventRepository.checkEventExistsById(eventId);
        commentRepository.checkCommentExistsById(comId);
        commentRepository.deleteById(comId);
    }

    private Comment getUpdatedComment(Long comId, UpdateCommentRequestDto commentDto) {
        Comment comment = commentRepository.getReferenceById(comId);

        comment.setText(commentDto.getText());

        return comment;
    }

    private Comment getComment(AddCommentRequestDto commentDto, Long userId, Long eventId) {
        User author = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        return CommentMapper.toComment(commentDto, author, event);
    }
}
