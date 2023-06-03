package com.gabriel.satix.api.models;

public class TicketInfoQR {
    private String url;

    public TicketInfoQR(String url) {
        this.url = url;
    }

    public TicketInfoQR() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
