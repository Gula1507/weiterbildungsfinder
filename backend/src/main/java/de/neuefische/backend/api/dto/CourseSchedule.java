package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseSchedule(
        @JsonProperty ("bezeichnung")
        String courseScheduleName
) {
}
