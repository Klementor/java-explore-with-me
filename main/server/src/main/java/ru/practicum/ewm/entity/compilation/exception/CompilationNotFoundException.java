package ru.practicum.ewm.entity.compilation.exception;

import javax.persistence.EntityNotFoundException;

public class CompilationNotFoundException extends EntityNotFoundException {
    public static final String COMPILATION_NOT_FOUND = "Compilation with id=%d was not found";

    public CompilationNotFoundException(String message) {
        super(message);
    }

    public static CompilationNotFoundException fromCompilationId(Long compId) {
        String message = String.format(COMPILATION_NOT_FOUND, compId);
        return new CompilationNotFoundException(message);
    }
}
