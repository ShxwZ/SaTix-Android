package com.gabriel.satix.api.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterResponse implements Serializable {
    private Map<String, String> errors;

    public RegisterResponse() {
        errors = new HashMap<>();
    }

    public RegisterResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public void addError(String fieldName, String errorMessage) {
        errors.put(fieldName, errorMessage);
    }
    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
