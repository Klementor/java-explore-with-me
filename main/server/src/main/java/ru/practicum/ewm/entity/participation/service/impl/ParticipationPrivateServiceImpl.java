package ru.practicum.ewm.entity.participation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.repository.EventJpaRepository;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;
import ru.practicum.ewm.entity.participation.entity.Participation;
import ru.practicum.ewm.entity.participation.mapper.ParticipationMapper;
import ru.practicum.ewm.entity.participation.repository.jpa.ParticipationRequestJpaRepository;
import ru.practicum.ewm.entity.participation.service.ParticipationPrivateService;
import ru.practicum.ewm.entity.participation.validation.validator.ParticipationValidator;
import ru.practicum.ewm.entity.user.entity.User;
import ru.practicum.ewm.entity.user.repository.UserJpaRepository;
import ru.practicum.ewm.exception.ConflictException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ParticipationPrivateServiceImpl implements ParticipationPrivateService {
    private final ParticipationRequestJpaRepository requestRepository;
    private final UserJpaRepository userRepository;
    private final EventJpaRepository eventRepository;

    @Override
    @Transactional
    public ParticipationResponseDto addRequest(Long requesterId, Long eventId) {
        User user = userRepository.checkUserExistsById(requesterId);
        Event event = eventRepository.checkEventExistsById(eventId);
        checkRequest(user, event);
        Participation request = getParticipationRequest(user, event);
        Participation savedRequest = requestRepository.save(request);
        log.debug("PARTICIPATION_REQUEST[id={}, event_id={}, requester_id={}, status='{}'] saved.",
                savedRequest.getId(),
                savedRequest.getEvent().getId(),
                savedRequest.getRequester().getId(),
                savedRequest.getStatus());
        return ParticipationMapper.toParticipationResponseDto(savedRequest);
    }

    @Override
    @Transactional
    public ParticipationResponseDto cancelRequestById(Long userId, Long requestId) {
        userRepository.checkUserExistsById(userId);
        Participation participation = requestRepository.checkParticipationExistsById(requestId);
        Participation canceledRequest = cancelRequest(participation);
        log.debug("PARTICIPATION_REQUEST[id={}, requester_id={}, event_id={}] canceled.",
                canceledRequest.getId(),
                canceledRequest.getRequester().getId(),
                canceledRequest.getEvent().getId());
        return ParticipationMapper.toParticipationResponseDto(canceledRequest);
    }

    @Override
    public Iterable<ParticipationResponseDto> getRequestsByRequesterId(Long userId) {
        userRepository.checkUserExistsById(userId);
        List<Participation> userRequests = requestRepository.findAllByRequesterId(userId);
        var userRequestDtos = ParticipationMapper.toParticipationResponseDto(userRequests);
        log.debug("PARTICIPATION_REQUEST_PAGE_BY_REQUESTER_ID<DTO>[requester_id={}, size={}] returned.",
                userId, userRequestDtos.size());
        return userRequestDtos;
    }

    private Participation getParticipationRequest(User user, Event event) {
        Participation request = new Participation();

        request.setRequester(user);
        request.setEvent(event);

        if (requestRepository.existsByEventIdAndRequesterId(event.getId(), user.getId())) {
            throw new ConflictException("Request already exists");
        }

        if (event.getRequestModeration() == Boolean.FALSE) {
            request.setStatus(Participation.Status.CONFIRMED);
        }

        return request;
    }

    private Participation cancelRequest(Participation participation) {

        participation.setStatus(Participation.Status.CANCELED);

        return participation;
    }

    private void checkRequest(User user, Event event) {
        Integer confirmedRequests = requestRepository.getEventRequestsCount(event.getId(), Participation.Status.CONFIRMED);

        ParticipationValidator.validateRequesterIsNotInitiator(user.getId(), event.getInitiator().getId());
        ParticipationValidator.validateEventPublished(event.getState());
        ParticipationValidator.validateLimitNotExceeded(1, confirmedRequests, event.getParticipantLimit());
    }
}
