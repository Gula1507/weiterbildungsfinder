package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseAddressDetails(
        @JsonProperty("plz")
        String postalCode,
        @JsonProperty("name")
        String city
) {
}
