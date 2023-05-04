package ru.practicum.ewm.entity.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.category.dto.request.AddCategoryRequestDto;
import ru.practicum.ewm.entity.category.dto.request.UpdateCategoryRequestDto;
import ru.practicum.ewm.entity.category.dto.response.CategoryResponseDto;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.category.mapper.CategoryMapper;
import ru.practicum.ewm.entity.category.repository.CategoryJpaRepository;
import ru.practicum.ewm.entity.category.service.CategoryAdminService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryJpaRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponseDto addCategory(AddCategoryRequestDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        log.debug("CATEGORY[id={}, name='{}'] saved.", category.getId(), category.getName());
        return CategoryMapper.toCategoryResponseDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long catId) {
        categoryRepository.checkCategoryExistsById(catId);
        categoryRepository.deleteById(catId);
        log.debug("CATEGORY[id={}] deleted.", catId);
    }

    private Category getUpdatedCategory(Category category, UpdateCategoryRequestDto categoryDto) {
        category.setName(categoryDto.getName());
        return category;
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategoryById(Long catId, UpdateCategoryRequestDto categoryDto) {
        Category updatedCategory = getUpdatedCategory(categoryRepository.checkCategoryExistsById(catId), categoryDto);
        log.debug("CATEGORY[id={}, name='{}'] updated.", updatedCategory.getId(), updatedCategory.getName());
        return CategoryMapper.toCategoryResponseDto(updatedCategory);
    }
}
