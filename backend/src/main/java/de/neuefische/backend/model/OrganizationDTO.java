package de.neuefische.backend.model;

import jakarta.validation.constraints.NotNull;

public record OrganizationDTO(
        @NotNull(message = "Name is mandatory")
        String name,
        String homepage,
        String email,
        String address
) {
}
