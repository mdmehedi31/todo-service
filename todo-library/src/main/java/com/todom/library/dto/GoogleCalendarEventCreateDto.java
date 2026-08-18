package com.todom.library.dto;

import java.time.Instant;
import java.util.List;

public class GoogleCalendarEventCreateDto {

    private List<String> inviteUserList;
    private String eventSummary;
    private String description;
    private Instant eventStartDate;
    private Instant eventEndDate;
    private String calendarId;
    private String location;

    public List<String> getInviteUserList() {
        return inviteUserList;
    }

    public void setInviteUserList(List<String> inviteUserList) {
        this.inviteUserList = inviteUserList;
    }

    public String getEventSummary() {
        return eventSummary;
    }

    public void setEventSummary(String eventSummary) {
        this.eventSummary = eventSummary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Instant eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Instant getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Instant eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
