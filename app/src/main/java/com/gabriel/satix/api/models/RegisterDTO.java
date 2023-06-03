package com.gabriel.satix.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterDTO  {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("passwordConfirmation")
    private String passwordConfirmation;
    @SerializedName("email")
    private String email;
    @SerializedName("dni")
    private String dni;
    @SerializedName("name")
    private String name;
    @SerializedName("lastName1")
    private String lastName1;
    @SerializedName("lastName2")
    private String lastName2;
    @SerializedName("phone")
     private String phone;
    @SerializedName("birthday")
    private String birthday;

    public RegisterDTO() {
    }

    public RegisterDTO(String username, String password, String passwordConfirmation, String email, String dni, String name, String lastName1, String lastName2, String phone, String birthday) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.email = email;
        this.dni = dni;
        this.name = name;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.phone = phone;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}