package ru.practicum.ewm.entity.event.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.event.dto.response.comment.CommentResponseDto;
import ru.practicum.ewm.entity.event.service.contoller.EventPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/events/{id}/comments")
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final EventPublicService publicEventService;

    @GetMapping
    public Iterable<CommentResponseDto> getEventComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        return publicEventService.getComments(id, from, size);
    }

    @GetMapping("/{comId}")
    public CommentResponseDto getEventCommentById(
            @PathVariable Long id,
            @PathVariable Long comId
    ) {
        return publicEventService.getCommentById(id, comId);
    }
}
