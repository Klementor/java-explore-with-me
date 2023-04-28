package ru.practicum.ewm.entity.participation.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.entity.participation.dto.response.ParticipationResponseDto;
import ru.practicum.ewm.entity.participation.entity.Participation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParticipationMapper {

    public static ParticipationResponseDto toParticipationResponseDto(Participation request) {
        ParticipationResponseDto requestDto = new ParticipationResponseDto();

        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setStatus(request.getStatus());
        requestDto.setCreated(request.getCreatedOn());

        return requestDto;
    }

    public static List<ParticipationResponseDto> toParticipationResponseDto(Iterable<Participation> requests) {
        return StreamSupport.stream(requests.spliterator(), false)
                .map(ParticipationMapper::toParticipationResponseDto)
                .collect(Collectors.toList());
    }
}
