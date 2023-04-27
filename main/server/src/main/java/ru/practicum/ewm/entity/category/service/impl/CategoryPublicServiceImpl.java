package ru.practicum.ewm.entity.category.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.category.dto.response.CategoryResponseDto;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.category.mapper.CategoryMapper;
import ru.practicum.ewm.entity.category.repository.CategoryJpaRepository;
import ru.practicum.ewm.entity.category.service.CategoryPublicService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicServiceImpl implements CategoryPublicService {
    private final CategoryJpaRepository categoryRepository;

    @Override
    public List<CategoryResponseDto> getCategories(Integer from, Integer size) {
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from, size));
        List<CategoryResponseDto> categoryDtos = CategoryMapper.toCategoryResponseDto(categories);
        log.debug("CATEGORY_PAGE<DTO>[from={}, size={}, categories_count={}] returned.",
                from, size, categoryDtos.size());
        return categoryDtos;
    }

    @Override
    public CategoryResponseDto getCategoryById(Long catId) {
        Category category = categoryRepository.checkCategoryExistsById(catId);
        CategoryResponseDto categoryDto = CategoryMapper.toCategoryResponseDto(category);
        log.debug("CATEGORY<DTO>[id={}, name='{}'] returned", categoryDto.getId(), categoryDto.getName());
        return categoryDto;
    }
}
