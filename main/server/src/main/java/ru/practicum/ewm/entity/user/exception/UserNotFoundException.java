package ru.practicum.ewm.entity.user.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public static final String USER_NOT_FOUND = "User with id=%d was not found";

    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException fromUserId(Long id) {
        return new UserNotFoundException(String.format(USER_NOT_FOUND, id));
    }
}
