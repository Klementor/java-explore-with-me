package ru.practicum.ewm.entity.event.exception.comment;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {
    public static final String COMMENT_NOT_FOUND = "Comment with id=%d was not found";

    public CommentNotFoundException(String message) {
        super(message);
    }

    public static CommentNotFoundException fromCommentId(Long eventId) {
        String message = String.format(COMMENT_NOT_FOUND, eventId);
        return new CommentNotFoundException(message);
    }
}
