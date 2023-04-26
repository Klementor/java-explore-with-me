package ru.practicum.ewm.entity.category.service;

import ru.practicum.ewm.entity.category.dto.request.AddCategoryRequestDto;
import ru.practicum.ewm.entity.category.dto.request.UpdateCategoryRequestDto;
import ru.practicum.ewm.entity.category.dto.response.CategoryResponseDto;

public interface CategoryAdminService {
    CategoryResponseDto updateCategoryById(Long catId, UpdateCategoryRequestDto categoryDto);

    CategoryResponseDto addCategory(AddCategoryRequestDto categoryDto);

    void deleteCategoryById(Long catId);
}
