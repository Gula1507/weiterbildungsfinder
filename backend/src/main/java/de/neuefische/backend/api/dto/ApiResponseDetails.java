package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseDetails(
        @JsonProperty("angebot")
        ApiResponseCourseOffer courseOffer
) {
}
