package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponse(
        @JsonProperty("_embedded")
        ApiResponseContent responseContent
) {
}
