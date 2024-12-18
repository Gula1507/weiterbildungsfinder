package de.neuefische.backend.organization.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrganizationDTO(
        @NotNull(message = "Name is mandatory")
        String name,
        String homepage,
        String email,
        String address,
        List<@Valid Review> reviews,
        double averageRating,
        List <Course> courses
) {
}
