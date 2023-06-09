package ru.practicum.ewm.entity.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
public class AddUserRequestDto {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
