package ru.practicum.ewm.entity.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.compilation.dto.request.AddCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.request.UpdateCompilationRequestDto;
import ru.practicum.ewm.entity.compilation.dto.response.CompilationResponseDto;
import ru.practicum.ewm.entity.compilation.service.CompilationAdminService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {
    private final CompilationAdminService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto addCompilation(@RequestBody
                                                     @Valid AddCompilationRequestDto compilationDto) {
        log.info("add EVENT_COMPILATION[events_count={}, pinned={}, title='{}'].",
                compilationDto.getEvents().size(),
                compilationDto.isPinned(),
                compilationDto.getTitle());
        return compilationService.addCompilation(compilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationResponseDto updateCompilationById(@PathVariable Long compId,
                                                        @RequestBody
                                                        @Valid UpdateCompilationRequestDto compilationDto) {
        log.info("update EVENT_COMPILATION[id={}, events_count={}, pinned={}, title='{}'].",
                compId,
                compilationDto.getEvents().size(),
                compilationDto.isPinned(),
                compilationDto.getTitle());
        return compilationService.updateCompilation(compId, compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable Long compId) {
        log.info("delete EVENT_COMPILATION[id={}].", compId);
        compilationService.deleteCompilationById(compId);
    }
}
