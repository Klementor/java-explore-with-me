package ru.practicum.ewm.entity.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.entity.event.dto.request.UpdateEventAdminRequestDto;
import ru.practicum.ewm.entity.event.dto.response.EventFullResponseDto;
import ru.practicum.ewm.entity.event.entity.Event;
import ru.practicum.ewm.entity.event.service.contoller.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
@Validated
@RequiredArgsConstructor
@Slf4j
public class EventAdminController {
    private final EventAdminService adminEventService;

    @GetMapping
    public Iterable<EventFullResponseDto> getEventsByParameters(@RequestParam(required = false) Set<Long> users,
                                                                @RequestParam(required = false) Set<Event.State> states,
                                                                @RequestParam(required = false) Set<Long> categories,
                                                                @RequestParam(required = false) LocalDateTime rangeStart,
                                                                @RequestParam(required = false) LocalDateTime rangeEnd,
                                                                @RequestParam(defaultValue = "0")
                                                                @PositiveOrZero Integer from,
                                                                @RequestParam(defaultValue = "10")
                                                                @Positive Integer size) {
        log.info("get EVENT_PAGE<DTO>[from={}, size={}, user_ids_count={}, states_count={}, categories_count={}, "
                        + "start_time={}, end_time={}] by parameters.",
                from, size, (users != null) ? users.size() : "ALL", (states != null) ? states.size() : "ALL",
                (categories != null) ? categories.size() : "ALL", rangeStart, rangeEnd);
        return adminEventService.searchAdminEventsByParameters(
                users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullResponseDto updateEventById(@PathVariable Long eventId,
                                                @RequestBody @Valid UpdateEventAdminRequestDto adminRequest) {
        log.info("update EVENT[id={}, state_action={}].", eventId, adminRequest.getStateAction());
        return adminEventService.updateAdminEventById(eventId, adminRequest);
    }
}
