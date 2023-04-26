package ru.practicum.ewm.entity.user.service;

import ru.practicum.ewm.entity.user.dto.request.AddUserRequestDto;
import ru.practicum.ewm.entity.user.dto.response.UserResponseDto;

import java.util.Set;

public interface UserAdminService {
    UserResponseDto addUser(AddUserRequestDto userDto);

    Iterable<UserResponseDto> getUsersByIds(Set<Long> ids, Integer from, Integer size);

    void deleteUserById(Long userId);
}
