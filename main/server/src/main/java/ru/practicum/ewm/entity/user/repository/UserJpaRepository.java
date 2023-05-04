package ru.practicum.ewm.entity.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.user.entity.User;
import ru.practicum.ewm.entity.user.exception.UserNotFoundException;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    default User checkUserExistsById(@NonNull Long userId) {
        try {
            return getReferenceById(userId);
        } catch (Exception e) {
            throw UserNotFoundException.fromUserId(userId);
        }
    }
}
