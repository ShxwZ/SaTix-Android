package com.gabriel.satix.api.models;

import java.util.List;

public class LoginApiResponse {
    private String username;
    private List<String> authorities;
    private String token;

    public LoginApiResponse() {
    }

    public LoginApiResponse(String username, List<String> authorities, String token) {
        this.username = username;
        this.authorities = authorities;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
