package com.gabriel.satix.models;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event implements Serializable {
    private Long id;
    private String name;
    private String address;
    private int max_assistants;
    private int assistants;
    private String description;
    private String start_date;
    private String finish_date;
    private int min_age;
    private Long organizer;

    public Event(Long id, String name, String address, int max_assistants, int assistants, String description, String start_date, String finish_date, int min_age, Long organizer) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.max_assistants = max_assistants;
        this.assistants = assistants;
        this.description = description;
        this.start_date = start_date;
        this.finish_date = finish_date;
        this.min_age = min_age;
        this.organizer = organizer;
    }

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMax_assistants() {
        return max_assistants;
    }

    public void setMax_assistants(int max_assistants) {
        this.max_assistants = max_assistants;
    }

    public int getAssistants() {
        return assistants;
    }

    public void setAssistants(int assistants) {
        this.assistants = assistants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public int getMin_age() {
        return min_age;
    }

    public void setMin_age(int min_age) {
        this.min_age = min_age;
    }

    public Long getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Long organizer) {
        this.organizer = organizer;
    }
    public String dateForEvent() {
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime dateStart = LocalDateTime.parse(start_date,formatter);
            LocalDateTime dateFinish = LocalDateTime.parse(finish_date,formatter);


            LocalDateTime now = LocalDateTime.now();

            if (dateFinish.isBefore(now)) {
                //return "Finalizado: " + dateStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                return "Finalizado";
            } else if (dateStart.isBefore(now) && dateFinish.isAfter(now)) {
                return "¡Activo! " + dateStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                //return "Activo";
            } else {
                return "Próximo: " + dateStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                //return "Próximamente";
            }
        } catch (Exception e){
            Log.e("Error:", e.getMessage());
            return "";
        }
    }

}
