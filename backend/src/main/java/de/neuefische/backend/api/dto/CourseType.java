package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseType(

        @JsonProperty("bezeichnung")
        String courseTypeName
) {
}
