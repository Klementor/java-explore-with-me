package ru.practicum.ewm.entity.compilation.service;

import ru.practicum.ewm.entity.compilation.dto.request.AddCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.request.UpdateCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto;

public interface CompilationAdminService {
    CompilationResponseDto updateCompilation(Long compId, UpdateCompilationRequestDto compilationDto);

    CompilationResponseDto addCompilation(AddCompilationRequestDto compilationDto);

    void deleteCompilationById(Long compId);
}
