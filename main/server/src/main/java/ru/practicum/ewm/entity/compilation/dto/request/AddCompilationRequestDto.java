package ru.practicum.ewm.entity.compilation.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class AddCompilationRequestDto {
    @NotNull
    private Set<Long> events;

    private Boolean pinned;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
