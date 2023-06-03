package com.gabriel.satix.api.models;

public class VerifyResponse {
    private String name;
    private String lastname1;
    private String lastname2;
    private String phone;
    private String dni;
    private String nameEvent;

    public VerifyResponse(String name, String lastname1, String lastname2, String phone, String dni, String nameEvent) {
        this.name = name;
        this.lastname1 = lastname1;
        this.lastname2 = lastname2;
        this.phone = phone;
        this.dni = dni;
        this.nameEvent = nameEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname1() {
        return lastname1;
    }

    public void setLastname1(String lastname1) {
        this.lastname1 = lastname1;
    }

    public String getLastname2() {
        return lastname2;
    }

    public void setLastname2(String lastname2) {
        this.lastname2 = lastname2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }
}
