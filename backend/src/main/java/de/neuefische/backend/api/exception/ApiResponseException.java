package de.neuefische.backend.api.exception;

public class ApiResponseException extends RuntimeException {
    public ApiResponseException() {
        super("Keine Details in der API-Antwort gefunden");
    }
}
