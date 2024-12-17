package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CourseFormat(
        @JsonProperty ("bezeichnung")
        String courseFormatName
) {


}
