package ru.practicum.ewm.entity.compilation.service;

import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto;

public interface CompilationPublicService {
    CompilationResponseDto getCompilationById(Long compId);

    Iterable<CompilationResponseDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size);
}
