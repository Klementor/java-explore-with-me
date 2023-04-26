package ru.practicum.ewm.entity.compilation.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.compilation.entity.Compilation;
import ru.practicum.ewm.entity.compilation.exception.CompilationNotFoundException;

public interface CompilationJpaRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);

    default void checkCompilationExistsById(@NonNull Long compId) {
        if (!existsById(compId)) {
            throw CompilationNotFoundException.fromCompilationId(compId);
        }
    }
}
