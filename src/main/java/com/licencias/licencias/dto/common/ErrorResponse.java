package com.licencias.licencias.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
    private List<FieldErrorDetail> errors;
    private String path;
    private Instant timestamp;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
