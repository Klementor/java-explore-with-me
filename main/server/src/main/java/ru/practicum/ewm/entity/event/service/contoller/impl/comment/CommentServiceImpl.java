package ru.practicum.ewm.entity.event.service.contoller.impl.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.event.entity.comment.Comment;
import ru.practicum.ewm.entity.event.mapper.comment.CommentMapper;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.event.repository.comment.CommentJpaRepository;
import ru.practicum.ewm.entity.event.service.contoller.CommentService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentJpaRepository commentRepository;
    private final EventJpaRepository eventRepository;

    @Override
    public Iterable<CommentResponseDto> getComments(Long id, Integer from, Integer size) {
        eventRepository.checkEventExistsById(id);
        List<Comment> comments = commentRepository.findAllByEventId(id, PageRequest.of(from, size));
        return CommentMapper.toCommentResponseDto(comments);
    }

    @Override
    public CommentResponseDto getCommentById(Long id, Long comId) {
        eventRepository.checkEventExistsById(id);
        commentRepository.checkCommentExistsById(comId);
        Comment comment = commentRepository.getReferenceById(comId);
        return CommentMapper.toCommentResponseDto(comment);
    }
}
