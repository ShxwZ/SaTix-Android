package com.gabriel.satix.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TicketQRRequest implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("event_id")
    private String event_id;

    public TicketQRRequest(String username, String event_id) {
        this.username = username;
        this.event_id = event_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
