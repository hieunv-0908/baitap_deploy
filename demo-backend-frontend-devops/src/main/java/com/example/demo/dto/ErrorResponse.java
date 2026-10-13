package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private int code;
    private String timestamp;

    public ErrorResponse(String error, int code) {
        this.error = error;
        this.code = code;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
}
