package com.todo.service.googlecalendarservice;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.todo.core.dbservice.IUserDBService;
import com.todo.core.entity.UserEntity;
import com.todo.core.exceptionhandler.Forbidden;
import com.todo.service.auth.google.IGoogleAuthService;
import com.todom.library.dto.*;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.todom.library.utils.DateTimeUtil;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleCalendarService implements IGoogleCalendarService {


    @Value("${google.application_name}")
    private String applicationName;

    private IGoogleAuthService googleAuthService;
    private IUserDBService userDBService;

    public GoogleCalendarService(IGoogleAuthService googleAuthService, IUserDBService userDBService) {
        this.googleAuthService = googleAuthService;
        this.userDBService = userDBService;
    }

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class);

    private Calendar getGoogleCalendarClient(Credential credential) throws IOException, GeneralSecurityException {
        try {
            log.info("From get google calendar client : {}", this.applicationName);
            return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    credential).setApplicationName(applicationName).build();

        } catch (IOException e) {
            throw e;
        } catch (GeneralSecurityException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private Calendar buildCalendarClient(String accessToken) throws GeneralSecurityException, IOException {
        try {
            HttpRequestInitializer initializer = request -> {
                request.getHeaders().setAuthorization("Bearer " + accessToken);
            };
            return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), initializer)
                    .setApplicationName(applicationName)
                    .build();

        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public ApiResponse createEvent(Integer boardId, Integer boardItemId, Integer userId, GoogleCalendarEventCreateDto createEventDto) {
        try {
            UserDTO userDTO = this.googleAuthService.checkGoogleAccessTokenIsValidOrElseGetNewToken(userId);
            if (userDTO != null && userDTO.getGoogleAccessToken() != null && userDTO.getGoogleRefreshToken() != null) {
                //   Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod());
                //  credential.setAccessToken(userDTO.getGoogleAccessToken());
                Calendar calendarClient = buildCalendarClient(userDTO.getGoogleAccessToken());//getGoogleCalendarClient(credential);
                if (calendarClient != null) {
                    Event event = new Event();
                    if (createEventDto.getEventSummary() != null && !createEventDto.getEventSummary().isBlank()) {
                        event.setSummary(createEventDto.getEventSummary());
                    }

                    if (createEventDto.getDescription() != null && !createEventDto.getDescription().isBlank()) {
                        event.setDescription(createEventDto.getDescription());
                    }

                    String eventStartDate = String.valueOf(createEventDto.getEventStartDate());
                    EventDateTime startDate = new EventDateTime();
                    startDate.setDateTime(DateTime.parseRfc3339(eventStartDate));
                    event.setStart(startDate);
                    String eventEndDate = String.valueOf(createEventDto.getEventEndDate());
                    EventDateTime endDate = new EventDateTime();
                    endDate.setDateTime(DateTime.parseRfc3339(eventEndDate));
                    event.setEnd(endDate);

                    List<EventAttendee> eventAttendeeList = new ArrayList<>();

                    for (String email : createEventDto.getInviteUserList()) {
                        EventAttendee eventAttendee = new EventAttendee();
                        eventAttendee.setEmail(email);
                        eventAttendeeList.add(eventAttendee);
                    }
                    event.setAttendees(eventAttendeeList);
                    if (event.getLocked() != null && !event.getLocation().isBlank()) {
                        event.setLocation(event.getLocation());
                    }

                    String calendarId = "primary";
                    if (createEventDto.getCalendarId() != null) {
                        calendarId = createEventDto.getCalendarId();
                    }
                    event = calendarClient.events().insert(calendarId, event).setSendNotifications(true).execute();

                    log.info("the created event id is : {} \n, cal Id : {} \n", event.getId(), event.getICalUID(), event.getSummary());
                    return new ApiResponse(SuccessOrFailedEnum.SUCCESS);
                }
            }

            return new ApiResponse(SuccessOrFailedEnum.FAILED, "Event create failed");
        } catch (Forbidden e) {
            log.error("Forbidden exception occured while creating event", e);
            return new ApiResponse(e.getMessage());
        } catch (IOException e) {
            log.error("An io exception occured while creating event", e);
            return new ApiResponse(SuccessOrFailedEnum.FAILED);
        } catch (Exception e) {
            log.error("An exception occured while creating event", e);
            return new ApiResponse(SuccessOrFailedEnum.FAILED);
        }

    }

    @Override
    public List<UserCalendarDto> getAllUserCalendar(Integer userId) {
        try {
            UserDTO userDTO = this.googleAuthService.checkGoogleAccessTokenIsValidOrElseGetNewToken(userId);
            if (userDTO != null) {
                Calendar client = buildCalendarClient(userDTO.getGoogleAccessToken());
                CalendarList calendarList = client.calendarList().list().setMinAccessRole("writer").execute();
                if (calendarList != null && calendarList.getItems().size() > 0) {
                    List<CalendarListEntry> calendarListEntry = calendarList.getItems();
                    List<UserCalendarDto> userCalendarDtos = calendarListEntry.stream().map(e -> new UserCalendarDto(e.getId(), e.getSummary())).
                            collect(Collectors.toList());
                    return userCalendarDtos;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("An exception occured while getting user calendar", e);
            return new ArrayList<>();
        }
    }

    @Override
    public ApiResponse checkIsValidToSetupCalendarSchedule(Integer userId) {
        try {
            ApiResponse response = new ApiResponse();
            UserEntity user = this.userDBService.getUserByID(userId);
            if (user != null && (user.getGoogleAccessToken() != null && !user.getGoogleAccessToken().isBlank()) &&
                    (user.getGoogleResfreshToken() != null && !user.getGoogleResfreshToken().isBlank())) {
                response.setMessage(SuccessOrFailedEnum.SUCCESS);
                return response;
            }

            response.setMessage(SuccessOrFailedEnum.FAILED);
            response.setCustomMessage("You need to connect google calendar for using calendar feature.");
            return response;
        } catch (Exception e) {
            log.error("An exception thrown : ", e);
            return new ApiResponse(SuccessOrFailedEnum.FAILED, "Service not available right now");
        }
    }

    @Override
    public List<CalendarEventDTO> getAllUsersEventsByUserId(Integer userId, Instant eventStartDate, Instant eventEndDate) {
        try {
            {
                UserDTO userDTO = this.googleAuthService.checkGoogleAccessTokenIsValidOrElseGetNewToken(userId);
                if (userDTO != null) {
                    Calendar client = buildCalendarClient(userDTO.getGoogleAccessToken());

                    DateTime startDate= DateTime.parseRfc3339(eventStartDate.toString());
                    DateTime endDate= DateTime.parseRfc3339(eventEndDate.toString());

                    Events event = client.events().list("primary")
                            .setTimeMin(startDate).
                            setTimeMax(endDate).
                            setOrderBy("startTime").
                            setSingleEvents(true).execute();

                    if(event!=null && event.getItems() != null && event.getItems().size() > 0) {
                        List<CalendarEventDTO> eventDTOList=convertEventListToCalendarEventDTOList(event.getItems());
                        return eventDTOList;
                    }

                    return new ArrayList<>();
                }

            }
            return List.of();
        } catch (GeneralSecurityException e) {
            log.error("An exception thrown when fetching events: ", e);
            return new ArrayList<>();
        } catch (IOException e) {
            log.error("An exception thrown when fetching events: ", e);
            return new ArrayList<>();
        }

    }

    private List<CalendarEventDTO> convertEventListToCalendarEventDTOList(List<Event> eventList) {

        List<CalendarEventDTO> calendarEventDTOList = new ArrayList<>();

        for(Event event : eventList) {

            List<AttendeeDTO> attendees = new ArrayList<>();

            for(EventAttendee eventAttendee : event.getAttendees()) {
                AttendeeDTO attendeeDTO= new AttendeeDTO();
               if(eventAttendee.getEmail() != null){
                   attendeeDTO.setEmail(eventAttendee.getEmail());
               }

               if(eventAttendee.getSelf() != null){
                   attendeeDTO.setSelf(eventAttendee.getSelf());
               }
               if(eventAttendee.getResponseStatus() != null){
                   attendeeDTO.setResponseStatus(eventAttendee.getResponseStatus());
               }
                attendees.add(attendeeDTO);
            }
            CalendarEventDTO calendarEventDTO= new CalendarEventDTO();
            calendarEventDTO.setSummary(event.getSummary());
            calendarEventDTO.setDescription(event.getDescription());
            calendarEventDTO.setId(event.getId());
            calendarEventDTO.setCalendarUid(event.getICalUID());
            calendarEventDTO.setCreator(event.getCreator().getEmail());
            calendarEventDTO.setOrganizer(event.getOrganizer().getEmail());
            LocalDateTime endTime = DateTimeUtil.convertEpochMilliToLocalDateTimeWithUserTimeZone(event.getEnd().getDateTime().getValue(), event.getEnd().getTimeZone());
            calendarEventDTO.setEnd(endTime);
            LocalDateTime startTime =  DateTimeUtil.convertEpochMilliToLocalDateTimeWithUserTimeZone(event.getStart().getDateTime().getValue(), event.getStart().getTimeZone());
            calendarEventDTO.setStartDate(startTime);
            calendarEventDTO.setAttendees(attendees);
            LocalDateTime createdAt = DateTimeUtil.convertEpochMilliToLocalDateTimeWithUserTimeZone(event.getCreated().getValue(),null);
            calendarEventDTO.setCreated(createdAt);

           // calendarEventDTO.setTimeZone(event.getEnd().getTimeZone());
            String timeZone = event.getEnd().getTimeZone();
            calendarEventDTO.setTimeZone(timeZone);
            calendarEventDTOList.add(calendarEventDTO);
        }

        return calendarEventDTOList;
    }
}
