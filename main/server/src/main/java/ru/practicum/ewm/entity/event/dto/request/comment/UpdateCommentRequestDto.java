package ru.practicum.ewm.entity.event.dto.request.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCommentRequestDto {
    @Size(min = 3, max = 250)
    @NotBlank
    private String text;
}
