package ru.practicum.ewm.entity.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.category.exception.CategoryNotFoundException;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    default void checkCategoryExistsById(Long catId) {
        if (!existsById(catId)) {
            throw CategoryNotFoundException.fromCategoryId(catId);
        }
    }
}
