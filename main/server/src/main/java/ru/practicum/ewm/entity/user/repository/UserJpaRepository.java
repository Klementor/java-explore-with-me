package ru.practicum.ewm.entity.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.user.entity.User;
import ru.practicum.ewm.entity.user.exception.UserNotFoundException;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    default void checkUserExistsById(@NonNull Long userId) {
        if (!existsById(userId)) {
            throw UserNotFoundException.fromUserId(userId);
        }
    }
}
