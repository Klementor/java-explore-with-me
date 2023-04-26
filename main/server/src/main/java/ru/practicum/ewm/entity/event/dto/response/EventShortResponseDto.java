package ru.practicum.ewm.entity.event.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.entity.category.entity.Category;
import ru.practicum.ewm.entity.user.entity.User;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class EventShortResponseDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Long views;
    private Integer confirmedRequests;

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
}
