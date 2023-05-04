package ru.practicum.ewm.entity.event.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.participation.entity.Participation;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class EventRequestsByStatusResponseDto {
    private List<ParticipationDto> confirmedRequests;
    private List<ParticipationDto> rejectedRequests;

    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ParticipationDto {
        private Long id;
        private Long requester;
        private Long event;
        private Participation.Status status;
        private LocalDateTime created;

        public static ParticipationDto fromParticipation(Participation participation) {
            ParticipationDto participationDto = new ParticipationDto();

            participationDto.setId(participation.getId());
            participationDto.setRequester(participation.getRequester().getId());
            participationDto.setEvent(participation.getEvent().getId());
            participationDto.setStatus(participation.getStatus());
            participationDto.setCreated(participation.getCreatedOn());

            return participationDto;
        }
    }
}
