package com.todo.service.controller;


import com.google.api.services.calendar.model.Event;
import com.todo.service.googlecalendarservice.GoogleCalendarService;
import com.todo.service.googlecalendarservice.IGoogleCalendarService;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.CalendarEventDTO;
import com.todom.library.dto.GoogleCalendarEventCreateDto;
import com.todom.library.dto.UserCalendarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("calendar")
public class GoogleCalendarAddController {

    private final IGoogleCalendarService googleCalendarService;

    public GoogleCalendarAddController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @PostMapping("/event-create/{boardId}/{boardItemId}/{userId}")
    public ResponseEntity<ApiResponse> createEvent(@PathVariable Integer boardId, @PathVariable Integer boardItemId, @PathVariable Integer userId, @RequestBody GoogleCalendarEventCreateDto calendarEventCreateDto) {
        ApiResponse response = googleCalendarService.createEvent(boardId, boardItemId, userId, calendarEventCreateDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user-calendar-list/{userId}")
    public ResponseEntity<List<UserCalendarDto>> getUserCalendar(@PathVariable Integer userId) {
        List<UserCalendarDto> userCalendarDtoList = this.googleCalendarService.getAllUserCalendar(userId);
        return new ResponseEntity<>(userCalendarDtoList, HttpStatus.OK);
    }

    @GetMapping("/check-is-valid-to-set-calendar-date/{userId}")
    public ResponseEntity<ApiResponse> checkIsUserIsValidToSetDateInCalendar(@PathVariable Integer userId) {
        ApiResponse response = this.googleCalendarService.checkIsValidToSetupCalendarSchedule(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-all-user-events")
    public ResponseEntity<List<CalendarEventDTO>> getAllUserEvents(@RequestParam Integer userId, @RequestParam Instant eventStartDate, @RequestParam Instant eventEndDate) {
        List<CalendarEventDTO> eventList = this.googleCalendarService.getAllUsersEventsByUserId(userId, eventStartDate, eventEndDate);
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

}
