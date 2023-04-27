package ru.practicum.ewm.entity.participation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;
import ru.practicum.ewm.entity.participation.entity.Participation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@UtilityClass
public final class ParticipationMapper {

    public ParticipationResponseDto toParticipationResponseDto(Participation request) {
        ParticipationResponseDto requestDto = new ParticipationResponseDto();

        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setStatus(request.getStatus());
        requestDto.setCreated(request.getCreatedOn());

        return requestDto;
    }

    public List<ParticipationResponseDto> toParticipationResponseDto(Iterable<Participation> requests) {
        return StreamSupport.stream(requests.spliterator(), false)
                .map(ParticipationMapper::toParticipationResponseDto)
                .collect(Collectors.toList());
    }
}
