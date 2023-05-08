package ru.practicum.ewm.entity.event.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.event.dto.request.comment.AddCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.request.comment.UpdateCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.event.service.contoller.EventPrivateService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class AdminCommentController {

    private final EventPrivateService privateEventService;

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid AddCommentRequestDto commentDto
    ) {
        return privateEventService.addComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{eventId}/comments/{comId}")
    public CommentResponseDto updateCommentById(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long comId,
            @RequestBody @Valid UpdateCommentRequestDto commentDto
    ) {
        return privateEventService.updateCommentById(userId, eventId, comId, commentDto);
    }

    @DeleteMapping("/{eventId}/comments/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long comId
    ) {
        privateEventService.deleteCommentById(userId, eventId, comId);
    }
}
