package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseCourseOffer(
        @JsonProperty("bildungsanbieter")
        ApiResponseOrganization apiResponseOrganization
) {
}