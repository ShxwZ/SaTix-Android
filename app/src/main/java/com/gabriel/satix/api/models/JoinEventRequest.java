package com.gabriel.satix.api.models;

public class JoinEventRequest {
    private String username;
    private Long eventId;

    public JoinEventRequest() {
    }

    public JoinEventRequest(String username, Long eventId) {
        this.username = username;
        this.eventId = eventId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
