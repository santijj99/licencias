package com.licencias.licencias.exception;

import org.springframework.http.HttpStatus;

public class LicenseValidationException extends BusinessException {

    public LicenseValidationException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.FORBIDDEN);
    }
}
