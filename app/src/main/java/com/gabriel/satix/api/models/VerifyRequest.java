package com.gabriel.satix.api.models;

import com.google.gson.annotations.SerializedName;

public class VerifyRequest {
    @SerializedName("eventId")
    private String eventId;
    @SerializedName("code")
    private String code;
    @SerializedName("userId")
    private String userId;

    public VerifyRequest() {
    }

    public VerifyRequest(String eventId, String code, String userId) {
        this.eventId = eventId;
        this.code = code;
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
