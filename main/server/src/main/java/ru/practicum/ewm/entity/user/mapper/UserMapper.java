package ru.practicum.ewm.entity.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.entity.user.dto.request.AddUserRequestDto;
import ru.practicum.ewm.entity.user.dto.response.UserResponseDto;
import ru.practicum.ewm.entity.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static User toUser(AddUserRequestDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userDto = new UserResponseDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static List<UserResponseDto> toUserResponseDto(Iterable<User> users) {
        return StreamSupport.stream(users.spliterator(), false)
                .map(UserMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }
}
