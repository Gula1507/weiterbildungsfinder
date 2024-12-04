package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseOrganization(
        Long id,
        String name,
        String homepage,
        String email,
        @JsonProperty("adresse")
        ApiResponseAddress address

) {
}
