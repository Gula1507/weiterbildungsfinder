package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseDuration(
        @JsonProperty("bezeichnung")
        String durationName
) {
}
