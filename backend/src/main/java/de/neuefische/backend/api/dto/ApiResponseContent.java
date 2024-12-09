package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ApiResponseContent(
        @JsonProperty("termine")
        List<ApiResponseDetails> details
) {
}
