package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseAddress(
        @JsonProperty("strasse")
        String streetAndHomeNumber,
        @JsonProperty("ortStrasse")
        ApiResponseAddressDetails addressDetails
) {
}
