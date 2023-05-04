package ru.practicum.ewm.entity.category.exception;

import javax.persistence.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
    public static final String CATEGORY_NOT_FOUND = "Category with id=%d was not found";

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public static CategoryNotFoundException fromCategoryId(Long catId) {
        String message = String.format(CATEGORY_NOT_FOUND, catId);
        return new CategoryNotFoundException(message);
    }
}
