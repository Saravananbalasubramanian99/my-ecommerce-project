package com.ecommerce.backend.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse {

    private int status;

    private String message;

    private LocalDateTime timestamp;

    private Map<String, String> errors;		//To hold/store all the validation errors using key,value pair

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp,
            Map<String, String> errors) {

        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}