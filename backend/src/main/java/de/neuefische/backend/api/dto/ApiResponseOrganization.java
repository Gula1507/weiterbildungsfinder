package de.neuefische.backend.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record ApiResponseOrganization(
        Long id,
        String name,
        String homepage,
        String email,
        @JsonProperty("adresse")
        ApiResponseAddress address

) {
        @Override
        public boolean equals(Object o) {
                if (this == o)
                        return true;
                if (o == null || getClass() != o.getClass())
                        return false;
                ApiResponseOrganization that = (ApiResponseOrganization) o;
                return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
                return Objects.hashCode(id);
        }
}
