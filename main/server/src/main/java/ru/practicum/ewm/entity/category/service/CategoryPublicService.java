package ru.practicum.ewm.entity.category.service;

import ru.practicum.ewm.entity.category.dto.response.CategoryResponseDto;

public interface CategoryPublicService {
    Iterable<CategoryResponseDto> getCategories(Integer from, Integer size);

    CategoryResponseDto getCategoryById(Long catId);
}
