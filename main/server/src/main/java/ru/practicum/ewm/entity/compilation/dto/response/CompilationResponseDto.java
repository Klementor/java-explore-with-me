package ru.practicum.ewm.entity.compilation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class CompilationResponseDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;

    @JsonInclude(Include.NON_NULL)
    @Getter
    @Setter
    @NoArgsConstructor
    public static class EventShortDto {
        private Long id;
        private String title;
        private String annotation;
        private CategoryDto category;
        private Boolean paid;
        private LocalDateTime eventDate;
        private UserShortDto initiator;
        private Long views;
        private Long confirmedRequests;

        public static EventShortDto fromEvent(Event event) {
            EventShortDto eventDto = new EventShortDto();

            eventDto.setId(event.getId());
            eventDto.setTitle(event.getTitle());
            eventDto.setAnnotation(event.getAnnotation());
            eventDto.setPaid(event.getPaid());
            eventDto.setEventDate(event.getEventDate());
            eventDto.setCategory(CategoryDto.fromCategory(event.getCategory()));
            eventDto.setInitiator(UserShortDto.fromUser(event.getInitiator()));

            return eventDto;
        }

        @JsonInclude(Include.NON_NULL)
        @Getter
        @Setter
        @NoArgsConstructor
        public static class UserShortDto {
            private Long id;
            private String name;

            public static UserShortDto fromUser(User user) {
                UserShortDto userDto = new UserShortDto();

                userDto.setId(user.getId());
                userDto.setName(user.getName());

                return userDto;
            }
        }

        @JsonInclude(Include.NON_NULL)
        @Getter
        @Setter
        @NoArgsConstructor
        public static class CategoryDto {
            private Long id;
            private String name;

            public static CategoryDto fromCategory(Category category) {
                CategoryDto categoryDto = new CategoryDto();

                categoryDto.setId(category.getId());
                categoryDto.setName(category.getName());

                return categoryDto;
            }
        }
    }
}
