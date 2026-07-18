package com.licencias.licencias.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " no encontrado: " + id, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
