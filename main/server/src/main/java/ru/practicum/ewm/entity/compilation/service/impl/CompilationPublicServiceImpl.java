package ru.practicum.ewm.entity.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto;
import ru.practicum.ewm.entity.compilation.entity.Compilation;
import ru.practicum.ewm.entity.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.entity.compilation.repository.CompilationJpaRepository;
import ru.practicum.ewm.entity.compilation.service.CompilationPublicService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationJpaRepository compilationRepository;

    @Override
    public CompilationResponseDto getCompilationById(Long compId) {
        compilationRepository.checkCompilationExistsById(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        var compilationDto = CompilationMapper.toCompilationResponseDto(compilation, null, null);
        return compilationDto;
    }

    @Override
    public Iterable<CompilationResponseDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size) {
        Page<Compilation> compilations;
        Pageable pageable = PageRequest.of(from, size);
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinnedIs(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable);
        }
        var compilationDtos = CompilationMapper.toCompilationResponseDto(compilations, null, null);
        return compilationDtos;
    }
}
