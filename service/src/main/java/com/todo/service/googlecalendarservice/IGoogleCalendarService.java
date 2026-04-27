package com.todo.service.googlecalendarservice;

import com.google.api.services.calendar.model.Event;
import com.todom.library.dto.ApiResponse;
import com.todom.library.dto.CalendarEventDTO;
import com.todom.library.dto.GoogleCalendarEventCreateDto;
import com.todom.library.dto.UserCalendarDto;

import java.time.Instant;
import java.util.List;

public interface IGoogleCalendarService {

    public ApiResponse createEvent(Integer boardId, Integer boardItemId, Integer userId, GoogleCalendarEventCreateDto createEventDto);
    public List<UserCalendarDto> getAllUserCalendar(Integer userId);
    public ApiResponse  checkIsValidToSetupCalendarSchedule(Integer userId);
    public List<CalendarEventDTO>  getAllUsersEventsByUserId(Integer userId, Instant eventStartDate, Instant eventEndDate);
}
