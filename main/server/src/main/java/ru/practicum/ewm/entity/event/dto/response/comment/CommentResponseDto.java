package ru.practicum.ewm.entity.event.dto.response.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String text;
    private Long authorId;
    private Long eventId;
}
