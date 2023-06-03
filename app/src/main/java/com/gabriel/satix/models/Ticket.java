package com.gabriel.satix.models;


public class Ticket {
    private int id;
    private Event id_event;
    private String code_qr;
    private boolean used;

    public Ticket(int id, Event id_event, String code_qr, boolean used) {
        this.id = id;
        this.id_event = id_event;
        this.code_qr = code_qr;
        this.used = used;
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getId_event() {
        return id_event;
    }

    public void setId_event(Event id_event) {
        this.id_event = id_event;
    }

    public String getCode_qr() {
        return code_qr;
    }

    public void setCode_qr(String code_qr) {
        this.code_qr = code_qr;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
