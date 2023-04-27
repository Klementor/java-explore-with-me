package ru.practicum.ewm.entity.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.compilation.dto.request.AddCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.request.UpdateCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto;
import ru.practicum.ewm.entity.compilation.entity.Compilation;
import ru.practicum.ewm.entity.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.entity.compilation.repository.CompilationJpaRepository;
import ru.practicum.ewm.entity.compilation.service.CompilationAdminService;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationJpaRepository compilationRepository;
    private final EventJpaRepository eventRepository;

    @Override
    @Transactional
    public CompilationResponseDto addCompilation(AddCompilationRequestDto compilationDto) {
        Set<Event> existsEvents = new HashSet<>(eventRepository.findAllById(compilationDto.getEvents()));
        Compilation compilation = CompilationMapper.toCompilation(compilationDto, existsEvents);
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.debug("COMPILATION[id={}, title='{}', pinned={}, events_count={}] returned.",
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                (compilation.getEvents() != null) ? compilation.getEvents().size() : 0);
        return CompilationMapper.toCompilationResponseDto(savedCompilation, null, null);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        compilationRepository.checkCompilationExistsById(compId);
        compilationRepository.deleteById(compId);
        log.debug("COMPILATION[id={}] deleted", compId);
    }

    @Override
    @Transactional
    public CompilationResponseDto updateCompilation(Long compId, UpdateCompilationRequestDto compilationDto) {
        Compilation updatedCompilation = getUpdatedCompilation(compilationRepository.checkCompilationExistsById(compId),
                compilationDto);
        log.debug("COMPILATION[id={}, title='{}', pinned={}, events_count={}] updated.",
                updatedCompilation.getId(),
                updatedCompilation.getTitle(),
                updatedCompilation.getPinned(),
                (updatedCompilation.getEvents() != null) ? updatedCompilation.getEvents().size() : 0);
        return CompilationMapper.toCompilationResponseDto(updatedCompilation, null, null);
    }

    private Compilation getUpdatedCompilation(Compilation compilation, UpdateCompilationRequestDto compilationDto) {

        Optional.ofNullable(compilationDto.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(compilationDto.getPinned()).ifPresent(compilation::setPinned);

        Set<Event> existsEvents = new HashSet<>(eventRepository.findAllById(compilationDto.getEvents()));
        compilation.setEvents(existsEvents);

        return compilation;
    }
}
