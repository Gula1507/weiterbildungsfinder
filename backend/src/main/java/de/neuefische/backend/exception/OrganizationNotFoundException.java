package de.neuefische.backend.exception;

public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String id) {
        super("Weiterbildungsanbieter mit id: " + id + " nicht gefunden");
    }
}