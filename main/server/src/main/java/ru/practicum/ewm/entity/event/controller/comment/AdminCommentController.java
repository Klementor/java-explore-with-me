package ru.practicum.ewm.entity.event.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.event.dto.request.comment.AddCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.request.comment.UpdateCommentRequestDto;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.event.service.contoller.CommentAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@Validated
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentAdminService adminCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid AddCommentRequestDto commentDto) {
        return adminCommentService.addComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{comId}")
    public CommentResponseDto updateCommentById(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long comId,
            @RequestBody @Valid UpdateCommentRequestDto commentDto) {
        return adminCommentService.updateCommentById(userId, eventId, comId, commentDto);
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long comId) {
        adminCommentService.deleteCommentById(userId, eventId, comId);
    }
}
